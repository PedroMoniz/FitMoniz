package com.moniz.fitmoniz

import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.retryWhen
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.selects.onTimeout
import kotlinx.coroutines.selects.select
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import java.io.IOException
import java.util.PriorityQueue

data class FormData(val title: String, val content: String)

sealed interface SaveResult {
    object Idle : SaveResult
    object Saving : SaveResult
    object Success : SaveResult
    data class Error(val message: String) : SaveResult
}

interface FormRepository {
    suspend fun saveDraft(data: FormData)
}

class AutoSaveFormProcessor(
    private val repository: FormRepository
) {
    /**
     * Processes raw UI form emissions into a stream of save statuses.
     */

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    fun processAutoSave(formDataFlow: Flow<FormData>): Flow<SaveResult> {

        return formDataFlow
            .debounce(timeoutMillis = 500)
            .distinctUntilChanged()
            .flatMapLatest {
                flow {
                    emit(SaveResult.Saving)
                    try {
                        repository.saveDraft(it)
                        emit(SaveResult.Success)
                    } catch (e: Exception) {
                        emit(SaveResult.Error(e.message ?: "Log not saved"))
                    }
                }
            }
            .onStart { emit(SaveResult.Idle) }
    }
}


interface TimeProvider {
    fun currentTimeMillis(): Long
}

class TtlLruCache<K, V>(
    private val capacity: Int,
    private val ttlMillis: Long,
    private val timeProvider: TimeProvider
) {

    private val mutex = Mutex()

    val cache = object : LinkedHashMap<K, Pair<V, Long>>(capacity, 0.75f, true) {
        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<K, Pair<V, Long>>?): Boolean {
            return size > capacity
        }
    }

    /**
     * Inserts or updates the value for the given key.
     */
    suspend fun put(key: K, value: V) {
        mutex.withLock {
            cache[key] = Pair(value, timeProvider.currentTimeMillis())
        }
    }

    /**
     * Retrieves the value for the key if it exists and is not expired.
     */
    suspend fun get(key: K): V? {
        return mutex.withLock {
            val item: Pair<V, Long> = cache[key] ?: return@withLock null

            return if (item.second - timeProvider.currentTimeMillis() > ttlMillis) {
                cache.remove(key)
                null
            } else {
                cache[key] = Pair(item.first, timeProvider.currentTimeMillis())
                item.first
            }
        }
    }
}


fun <T> Flow<T>.retryWithExponentialBackoff(
    maxRetries: Int = 3,
    initialDelayMillis: Long = 1000L,
    maxDelayMillis: Long = 10000L
): Flow<T> {
    return this.retryWhen { cause, attempt ->
        if (attempt > maxRetries || cause !is IOException) {
            return@retryWhen false
        }
        delay(
            timeMillis = maxDelayMillis.coerceAtMost(
                Math.powExact(
                    initialDelayMillis,
                    attempt.toInt()
                )
            )
        )
        true
    }
}


data class UserProfile(val id: String, val name: String, val avatarUrl: String)
data class Notification(val id: String, val title: String, val isRead: Boolean)
enum class NetworkStatus { ONLINE, OFFLINE }

sealed interface DashboardUiState {
    object Loading : DashboardUiState
    data class Success(
        val userName: String,
        val avatarUrl: String,
        val unreadCount: Int,
        val isOffline: Boolean
    ) : DashboardUiState
}

class DashboardViewModel(
    private val scope: CoroutineScope
) {
    /**
     * Combines multiple reactive streams into a production-ready StateFlow for Compose.
     */
    fun combineDashboardState(
        userFlow: Flow<UserProfile?>,
        notificationsFlow: Flow<List<Notification>>,
        networkStatusFlow: Flow<NetworkStatus>
    ): StateFlow<DashboardUiState> {


        return combine(
            userFlow.distinctUntilChanged(),
            notificationsFlow.distinctUntilChanged(),
            networkStatusFlow.distinctUntilChanged()
        ) { user, notifications, network ->

            if (user == null) {
                return@combine DashboardUiState.Loading
            }

            DashboardUiState.Success(
                userName = user.name,
                avatarUrl = user.avatarUrl,
                unreadCount = notifications.count { !it.isRead },
                isOffline = network == NetworkStatus.OFFLINE
            )
        }
            .distinctUntilChanged()
            .stateIn(
                scope = scope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = DashboardUiState.Loading
            )
    }
}


@OptIn(DelicateCoroutinesApi::class, ExperimentalCoroutinesApi::class)
fun <T> Flow<T>.bufferByTimeOrSize(
    timespanMillis: Long,
    maxBatchSize: Int
): Flow<List<T>> = flow {
    coroutineScope {

        val channel = Channel<T>(Channel.UNLIMITED)

        val producer = launch {
            try {
                collect { channel.send(it) }
            } finally {
                channel.close()
            }
        }

        val currentBatch = mutableListOf<T>()
        var timeoutJob: Job? = null

        suspend fun emitBatch() {
            if (currentBatch.isNotEmpty()) {
                emit(currentBatch.toList())
                currentBatch.clear()
            }
            timeoutJob?.cancel()
            timeoutJob = null
        }

        while (!channel.isClosedForReceive) {
            if (currentBatch.isEmpty()) {
                val item = channel.receiveCatching().getOrNull()
                if (item != null) {
                    currentBatch.add(item)
                }
            } else {
                val timedOut = select {
                    channel.onReceiveCatching { result ->
                        val item = result.getOrNull()
                        if (item != null) {
                            currentBatch.add(item)
                        }
                        false
                    }
                    onTimeout(timeMillis = timespanMillis) { true }
                }

                if (timedOut) {
                    emitBatch()
                }
            }

            if (currentBatch.size >= maxBatchSize) {
                emitBatch()
            }
        }

        emitBatch()
        producer.cancel()
    }
}


sealed interface SyncState {
    object Idle : SyncState
    object Syncing : SyncState
    data class Success(val itemsSynced: Int) : SyncState
    data class Error(val throwable: Throwable) : SyncState
}

interface DataSyncer {
    suspend fun syncPendingData(): Int
}

class NetworkSyncManager(
    private val dataSyncer: DataSyncer
) {
    private val _syncState = MutableStateFlow<SyncState>(SyncState.Idle)
    val syncState: StateFlow<SyncState> = _syncState.asStateFlow()

    suspend fun observeNetworkChanges(networkStatusFlow: Flow<NetworkStatus>) {
        networkStatusFlow
            .distinctUntilChanged() // Ignore duplicate consecutive network states
            .drop(1) // Skip initial state on launch
            .collect { status ->
                if (status == NetworkStatus.ONLINE) {
                    performSync()
                }
            }
    }

    private suspend fun performSync() {
        // Using suspending .emit() inside suspend functions
        _syncState.emit(SyncState.Syncing)
        try {
            val count = dataSyncer.syncPendingData()
            _syncState.emit(SyncState.Success(count))
        } catch (e: Exception) {
            _syncState.emit(SyncState.Error(e))
        }
    }
}

class Debouncer(
    private val timeoutMillis: Long,
    private val scope: CoroutineScope
) {
    private var debounceJob: Job? = null
    private val mutex = Mutex()

    fun submit(action: suspend () -> Unit) {
        scope.launch {
            mutex.withLock {
                // Cancel any previously scheduled job
                debounceJob?.cancel()

                // Launch new job after the delay window
                debounceJob = scope.launch {
                    delay(timeoutMillis)
                    action()
                }
            }
        }
    }

    fun cancel() {
        scope.launch {
            mutex.withLock {
                debounceJob?.cancel()
                debounceJob = null
            }
        }
    }
}

fun <T> Flow<T>.retryWithBackoff(
    maxRetries: Int = 3,
    initialDelayMs: Long = 1000L,
    factor: Double = 2.0,
    shouldRetry: (Throwable) -> Boolean = { it is IOException }
): Flow<T> {
    return this.retryWhen { cause, attempt ->
        if (attempt < maxRetries && shouldRetry(cause)) {
            // Calculate backoff: initialDelay * (factor ^ attempt)
            val currentDelay = (initialDelayMs * Math.pow(factor, attempt.toDouble())).toLong()
            delay(currentDelay)
            true // Trigger retry
        } else {
            false // Stop retrying, propagate error
        }
    }
}

sealed interface Resource<out T> {
    data class Loading<T>(val data: T? = null) : Resource<T>
    data class Success<T>(val data: T) : Resource<T>
    data class Error<T>(val message: String, val data: T? = null) : Resource<T>
}

interface LocalDataSource<T> {
    suspend fun getItems(): List<T>
    suspend fun saveItems(items: List<T>)
}

interface RemoteDataSource<T> {
    suspend fun fetchItems(): List<T>
}

class OfflineFirstRepository<T>(
    private val local: LocalDataSource<T>,
    private val remote: RemoteDataSource<T>
) {
    /**
     * Executes the offline-first sync flow.
     */
    fun getStream(): Flow<Resource<List<T>>> = flow {
        // 1. Load initial cached data
        val cachedData = local.getItems()
        emit(Resource.Loading(data = cachedData))

        try {
            // 2. Attempt remote fetch
            val remoteData = remote.fetchItems()

            // 3. Update local DB cache
            local.saveItems(remoteData)

            // 4. Emit fresh data from local source of truth
            val updatedData = local.getItems()
            emit(Resource.Success(data = updatedData))
        } catch (e: Exception) {
            // 5. On failure, preserve offline experience with cached data
            emit(
                Resource.Error(
                    message = e.message ?: "Network sync failed",
                    data = cachedData
                )
            )
        }
    }
}

// Provided Data Models (Do not modify)
sealed interface DownloadStatus {
    val url: String

    data class Pending(override val url: String) : DownloadStatus
    data class Downloading(override val url: String) : DownloadStatus
    data class Completed(override val url: String, val filePath: String) : DownloadStatus
    data class Failed(override val url: String, val error: String) : DownloadStatus
}

interface FileDownloader {
    suspend fun download(url: String): String
}

class DownloadQueueManager(
    private val downloader: FileDownloader,
    private val maxConcurrent: Int = 3
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    fun processQueue(urls: List<String>): Flow<DownloadStatus> {
        return urls.asFlow()
            .flatMapMerge(concurrency = maxConcurrent) { url ->
                flow {
                    emit(DownloadStatus.Downloading(url))
                    try {
                        val path = downloader.download(url)
                        emit(DownloadStatus.Completed(url, path))
                    } catch (e: Exception) {
                        emit(DownloadStatus.Failed(url, e.message ?: "Download failed"))
                    }
                }
            }
            .onStart {
                // Emits ALL Pending items before any download processing begins
                for (url in urls) {
                    emit(DownloadStatus.Pending(url))
                }
            }
    }
}

class RequestCoalescer<K, V> {
    private val mutex = Mutex()
    private val inFlightRequests = mutableMapOf<K, Deferred<V>>()

    /**
     * Executes [block] for [key], or awaits the result if an execution is already in flight.
     */
    suspend fun execute(key: K, block: suspend () -> V): V {
        val (deferred, isFirstCaller) = mutex.withLock {
            val existing = inFlightRequests[key]
            if (existing != null) {
                existing to false
            } else {
                // Launch deferred in current coroutineScope
                val created = coroutineScope {
                    async(start = kotlinx.coroutines.CoroutineStart.LAZY) { block() }
                }
                inFlightRequests[key] = created
                created to true
            }
        }

        // Start execution outside the lock if we created it
        if (isFirstCaller) {
            deferred.start()
        }

        return try {
            deferred.await()
        } finally {
            // Clean up the map once completed so subsequent calls fetch fresh data
            if (isFirstCaller) {
                mutex.withLock {
                    inFlightRequests.remove(key)
                }
            }
        }
    }
}

class RequestCoalescer2<K, V> {
    private val mutex = Mutex()
    private val inFlightRequests = mutableMapOf<K, CompletableDeferred<V>>()

    suspend fun execute(key: K, block: suspend () -> V): V {
        val (deferred, isFirstCaller) = mutex.withLock {
            val existing = inFlightRequests[key]
            if (existing != null) {
                existing to false
            } else {
                val newDeferred = CompletableDeferred<V>()
                inFlightRequests[key] = newDeferred
                newDeferred to true
            }
        }

        // Only the first caller executes the block outside of the lock
        if (isFirstCaller) {
            try {
                val result = block()
                deferred.complete(result)
            } catch (e: Throwable) {
                deferred.completeExceptionally(e)
            } finally {
                // Ensure map cleanup happens atomically so future calls make fresh requests
                mutex.withLock {
                    inFlightRequests.remove(key)
                }
            }
        }

        // All callers (first and deduplicated listeners) await the outcome
        return deferred.await()
    }
}

interface TransactionAggregator {
    data class Transaction(
        val category: String,
        val amountCent: Long,
        val timestampMs: Long
    )

    data class CategorySummary(
        val category: String,
        val totalAmountCent: Long
    )

    /**
     * Calculates the top [k] spending categories within [windowStartMs] and [windowEndMs] (inclusive).
     * Returns list sorted by totalAmountCent descending, then by category name alphabetically.
     */
    fun getTopCategories(
        transactions: List<Transaction>,
        windowStartMs: Long,
        windowEndMs: Long,
        k: Int
    ): List<CategorySummary>
}

class TransactionAggregatorImpl : TransactionAggregator {
    override fun getTopCategories(
        transactions: List<TransactionAggregator.Transaction>,
        windowStartMs: Long,
        windowEndMs: Long,
        k: Int
    ): List<TransactionAggregator.CategorySummary> {

        val summary = HashMap<String, Long>()

        transactions.forEach {
            if (it.timestampMs in windowStartMs..windowEndMs) {
                summary.merge(it.category, it.amountCent, Long::plus)
            }
        }

        if (summary.isEmpty()) return emptyList()

        val minHeap = PriorityQueue<TransactionAggregator.CategorySummary>(
            compareBy<TransactionAggregator.CategorySummary> { it.totalAmountCent }
                .thenByDescending { it.category } // Tie-breaker inverted for Min-Heap eviction
        )

        for ((category, total) in summary) {
            val summary = TransactionAggregator.CategorySummary(category, total)
            minHeap.offer(summary)
            if (minHeap.size > k) {
                minHeap.poll() // Evict lowest value
            }
        }

        // Sort descending by amount, then alphabetically by category name
        return minHeap.sortedWith(
            compareByDescending<TransactionAggregator.CategorySummary> { it.totalAmountCent }
        )
    }
}

fun main() {

    fun count(entry: String): Int {
        val seenLetters = mutableSetOf<Char>()
        val counter = LinkedHashMap<Char, Int>()

        for ((i, c) in entry.withIndex()) {
            if (seenLetters.contains(c)) {
                counter.remove(c)
            } else {
                seenLetters.add(c)
                counter[c] = i
            }
        }

        return counter.values.firstOrNull() ?: -1
    }


    fun countDuplicates(entry: IntArray): Int {

        val seenIds = mutableSetOf<Int>()
        val counter = mutableSetOf<Int>()

        for (id in entry) {
            if (seenIds.contains(id)) {
                counter.add(id)
            } else {
                seenIds.add(id)
            }
        }

        return counter.size

    }


    data class Interval(
        val start: Int,
        val end: Int
    )


    fun maxSessions(entries: List<Interval>): Int {
        var currentMax = 0
        for ((index, entry) in entries.withIndex()) {
            var entryMax = 0

        }
        return currentMax
    }


    fun solution(letters: String): Int {

        val seenLowerCase = mutableSetOf<Char>()
        val seenUpperCase = mutableSetOf<Char>()
        val seenLowerUpperCase = mutableSetOf<Char>()

        for (letter in letters) {
            if (letter.isLowerCase()) {
                if (seenUpperCase.contains(letter.uppercaseChar())) {
                    seenLowerUpperCase.remove(letter.uppercaseChar())
                }
                seenLowerCase.add(letter)
            }
            if (letter.isUpperCase()) {
                if (seenLowerCase.contains(letter.lowercaseChar()) &&
                    !seenUpperCase.contains(letter)
                ) {
                    seenLowerUpperCase.add(letter)
                }
                seenUpperCase.add(letter)
            }
        }
        return seenLowerUpperCase.size
    }


    fun squareCheck(A: Int, B: Int, squareSide: Int): Boolean {

        var tempA = A
        var tempB = B

        for (i in 1..4) {
            if (tempA - squareSide >= 0) {
                tempA -= squareSide
            } else if (tempB - squareSide >= 0) {
                tempB -= squareSide
            } else {
                return true
            }
        }

        return false
    }

    fun solution(A: Int, B: Int): Int {

        val totalDimension = A + B
        var squareSide = totalDimension / 4

        while (squareCheck(A, B, squareSide)) {
            squareSide--
        }
        return squareSide
    }



    data class UserDto(val id: String, val name: String)



}
