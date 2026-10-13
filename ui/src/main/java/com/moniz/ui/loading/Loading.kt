package com.moniz.ui.loading

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ContainedLoadingIndicator
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun Loading(modifier: Modifier = Modifier) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) { ContainedLoadingIndicator() }
}
