package com.moniz.app_dashboard.dashboard

import com.moniz.app_base.BaseViewModel
import com.moniz.app_base.events.EventBus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = DashboardViewModel.Factory::class)
internal class DashboardViewModel @AssistedInject constructor(
    private val eventBus: EventBus,
    @Assisted("accountId") private val accountId: String
) : BaseViewModel<DashboardViewModel.ComposeState>(initialState = ComposeState()) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("accountId") accountId: String
        ): DashboardViewModel
    }

    override suspend fun initOnSubscribe() {
        updateState { copy(viewState = ViewState.Loading) }
    }

    data class ComposeState(
        override val viewState: ViewState = ViewState.Input
    ) : ScreenState
}
