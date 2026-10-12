package com.moniz.app_dashboard.dashboard

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.moniz.app_base.navigation.BaseScreen

class DashboardScreen(val accountId: String) : BaseScreen {

    @Composable
    override fun Content() {
        val viewModel = hiltViewModel<DashboardViewModel, DashboardViewModel.Factory>(
            creationCallback = { factory ->
                factory.create(accountId = accountId)
            }
        )
        val composeState: DashboardViewModel.ComposeState by viewModel.stateStream.collectAsStateWithLifecycle()

        DashboardView(
            state = composeState
        )
    }
}
