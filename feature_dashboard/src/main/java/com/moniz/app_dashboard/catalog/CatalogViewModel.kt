package com.moniz.app_dashboard.catalog

import com.moniz.app_base.BaseViewModel
import com.moniz.app_base.events.EventBus
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
internal class CatalogViewModel @Inject constructor(private val eventBus: EventBus) :
    BaseViewModel<CatalogViewModel.ComposeState>(initialState = ComposeState()) {

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
