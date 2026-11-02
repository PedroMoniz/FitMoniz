package com.moniz.app_base

import androidx.compose.runtime.Stable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moniz.app_base.navigation.AppRoute
import com.moniz.app_base.navigation.NavigationHandler
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.onSubscription
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.reflect.KClass

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
            started = SharingStarted.WhileSubscribed(5000), //TimerConfig.FLOW_TIMEOUT),
            initialValue = _stateStream.value
        )

    val state: T
        get() = _stateStream.value

    fun updateState(reducer: T.() -> T) = _stateStream.update { state: T -> state.reducer() }

    open suspend fun initOnSubscribe() {}

    fun close() {
        viewModelScope.launch { navigationHandler.pop() }
    }

    fun launchNavigateTo(screen: AppRoute) =
        viewModelScope.launch { navigationHandler.navigateTo(screen) }

    suspend fun navigateTo(screen: AppRoute) = navigationHandler.navigateTo(screen)

    fun launchPopUntil(untilScreen: KClass<out AppRoute>? = null) {
        viewModelScope.launch { navigationHandler.pop(untilScreen = untilScreen) }
    }

    @Stable
    interface ScreenState {
        val isInitialLoading: Boolean
            get() = false
    }
}
