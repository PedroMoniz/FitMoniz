package com.moniz.app_dashboard.catalog

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CatalogScreen() {
    val viewModel = hiltViewModel<CatalogViewModel>()
    val composeState: CatalogViewModel.ComposeState by viewModel.stateStream.collectAsStateWithLifecycle()
    CatalogView(state = composeState)
}
