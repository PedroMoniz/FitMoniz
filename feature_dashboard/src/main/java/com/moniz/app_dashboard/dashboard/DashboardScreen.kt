package com.moniz.app_dashboard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DashboardScreen() {
    val viewModel = hiltViewModel<DashboardViewModel>()
    val composeState: DashboardViewModel.ComposeState by viewModel.stateStream.collectAsStateWithLifecycle()
    DashboardView(state = composeState)
}
