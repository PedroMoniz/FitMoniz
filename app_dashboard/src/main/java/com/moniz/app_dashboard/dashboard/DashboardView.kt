package com.moniz.app_dashboard.dashboard

import androidx.compose.runtime.Composable
import com.moniz.app_base.BaseViewModel
import com.moniz.ui.scaffold.MScaffold

@Composable
internal fun DashboardView(
    state: DashboardViewModel.ComposeState,
) {
    MScaffold(showLoading = state.viewState is BaseViewModel.ViewState.Loading) {

    }
}
