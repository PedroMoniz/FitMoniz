package com.moniz.app_dashboard.dashboard

import com.moniz.app_base.BaseViewModel
import com.moniz.app_base.events.EventBus
import com.moniz.app_base.utils.UiState
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.usecases.exercises.ObserveExercisesUseCase
import com.moniz.domain.utils.fold
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.distinctUntilChanged
import javax.inject.Inject

@HiltViewModel
internal class DashboardViewModel @Inject constructor(
    private val eventBus: EventBus,
    private val observeExercisesUseCase: ObserveExercisesUseCase
) : BaseViewModel<DashboardViewModel.ComposeState>(initialState = ComposeState()) {

    override suspend fun initOnSubscribe() {
        observeExercisesUseCase(Unit)
            .distinctUntilChanged()
            .collect { result ->
                result.fold(
                    onSuccess = { exercises: List<Exercise> ->
                        updateState { copy(exercises = UiState.Success(exercises)) }
                    },
                    onFailure = {
                        updateState { copy(exercises = UiState.Error(it)) }
                    },
                    onLoading = {
                        updateState { copy(exercises = UiState.Loading) }
                    }
                )
            }
    }

    data class ComposeState(
        val isLoading: Boolean = false,
        val exercises: UiState<List<Exercise>> = UiState.Loading
    ) : ScreenState {
        override val isInitialLoading: Boolean
            get() = exercises is UiState.Loading
    }
}
