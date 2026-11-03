package com.moniz.app_dashboard.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun DetailScreen(id: String) {
    val viewModel = hiltViewModel<DetailViewModel, DetailViewModel.Factory>(
        creationCallback = { factory -> factory.create(id) }
    )
    val composeState: DetailViewModel.ComposeState by viewModel.stateStream.collectAsStateWithLifecycle()
    DetailView(state = composeState)
}
