package com.moniz.app_dashboard.detail

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.moniz.ui.scaffold.MScaffold

@Composable
internal fun DetailView(
    state: DetailViewModel.ComposeState,
) {
    MScaffold(showLoading = state.isInitialLoading) {
        Text("Simple Scaffold Screen")
    }
}
