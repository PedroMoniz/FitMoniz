package com.moniz.app_dashboard.catalog

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.moniz.ui.scaffold.MScaffold

@Composable
internal fun CatalogView(
    state: CatalogViewModel.ComposeState,
) {
    MScaffold(showLoading = state.isInitialLoading) {
        Text("Simple Scaffold Screen")
    }
}
