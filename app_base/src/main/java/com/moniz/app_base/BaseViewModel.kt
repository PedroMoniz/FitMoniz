package com.moniz.app_base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

abstract class BaseViewModel<T : BaseViewModel.ScreenState>(initialState: T) : ViewModel() {

    @Inject
    lateinit var navigationHandler: NavigationHandler

    private var subscriptionJob: Job? = null
    private val _stateStream = MutableStateFlow(initialState)
    val stateStream = _stateStream
        .onSubscription {
            if (subscriptionJob?.isActive != true) {
                subscriptionJob = viewModelScope.launch { initOnSubscribe() }
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TimerConfig.FLOW_TIMEOUT),
            initialValue = _stateStream.value
        )

    val state: T
        get() = _stateStream.value

    fun updateState(reducer: T.() -> T) = _stateStream.update { state: T -> state.reducer() }

    open suspend fun initOnSubscribe() {}

    fun close() {
        viewModelScope.launch { navigationHandler.pop() }
    }

    fun launchNavigateTo(screen: BaseScreen) =
        viewModelScope.launch { navigationHandler.navigateTo(screen) }

    suspend fun navigateTo(screen: BaseScreen) = navigationHandler.navigateTo(screen)

    fun launchPopUntil(untilScreen: Class<out BaseScreen>? = null) {
        viewModelScope.launch { navigationHandler.pop(untilScreen = untilScreen) }
    }

    interface ScreenState {
        val viewState: ViewState
    }

    interface NavigableScreenState<V> : ScreenState {
        val currentView: V
    }

    sealed class ViewState {
        data object Loading : ViewState()
        data object Input : ViewState()
    }
}
