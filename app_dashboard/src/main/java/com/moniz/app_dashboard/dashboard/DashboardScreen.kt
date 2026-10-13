package com.moniz.app_dashboard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardScreen(accountId: String) {
    val viewModel = hiltViewModel<DashboardViewModel, DashboardViewModel.Factory>(
        creationCallback = { factory -> factory.create(accountId = accountId) }
    )
    val composeState: DashboardViewModel.ComposeState by viewModel.stateStream.collectAsStateWithLifecycle()
    DashboardView(state = composeState)
}
