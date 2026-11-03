package com.moniz.app_dashboard.detail

import com.moniz.app_base.BaseViewModel
import com.moniz.app_base.events.EventBus
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel

@HiltViewModel(assistedFactory = DetailViewModel.Factory::class)
internal class DetailViewModel @AssistedInject constructor(
    private val eventBus: EventBus,
    @Assisted("accountId") private val accountId: String
) : BaseViewModel<DetailViewModel.ComposeState>(initialState = ComposeState()) {

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("accountId") accountId: String
        ): DetailViewModel
    }

    override suspend fun initOnSubscribe() {
        updateState { copy(isLoading = true) }
    }

    data class ComposeState(
        val isLoading: Boolean = false
    ) : ScreenState {
        override val isInitialLoading: Boolean
            get() = isLoading
    }
}
