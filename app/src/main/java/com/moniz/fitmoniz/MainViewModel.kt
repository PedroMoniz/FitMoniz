package com.moniz.fitmoniz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.moniz.app_base.events.Event
import com.moniz.app_base.navigation.NavigationHandler
import com.moniz.app_base.utils.StateEvent
import com.moniz.app_base.utils.StateEventWithContent
import com.moniz.app_base.utils.Trigger
import com.moniz.app_base.utils.consumed
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import javax.inject.Inject


@HiltViewModel
class MainViewModel @Inject constructor(
    private val savedStateHandle: SavedStateHandle,
    val navigationHandler: NavigationHandler
) : ViewModel() {

    private val _stateStream: MutableStateFlow<ComposeState> =
        MutableStateFlow(savedStateHandle[STATE_KEY] ?: ComposeState())
    private val state: ComposeState
        get() = _stateStream.value

    private fun updateState(reducer: ComposeState.() -> ComposeState) {
        _stateStream.update { state: ComposeState ->
            val newState = state.reducer()
            savedStateHandle[STATE_KEY] = newState
            newState
        }
    }

    val stateStream = _stateStream.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000), //TimerConfig.FLOW_TIMEOUT),
        initialValue = _stateStream.value
    )


    //TODO: Remove the state events in favor of a sharedFlow
    data class ComposeState(
        val showRatingSheet: StateEventWithContent<Trigger.Type<Event.RatingSheet>> = consumed(),
        val showSnackbar: StateEventWithContent<Trigger.Type<Event.Snackbar>> = consumed(),
        val showNoInternet: StateEvent = consumed
    )

    companion object {
        private const val STATE_KEY = "compose_state"
    }
}
