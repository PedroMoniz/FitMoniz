package com.moniz.app_dashboard.dashboard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.moniz.ui.scaffold.MScaffold

@Composable
internal fun DashboardView(
    state: DashboardViewModel.ComposeState,
) {
    MScaffold(showLoading = state.isInitialLoading) {
        Text("Simple Scaffold Screen")
    }
}
