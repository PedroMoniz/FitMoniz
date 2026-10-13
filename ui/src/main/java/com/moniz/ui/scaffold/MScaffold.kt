package com.moniz.ui.scaffold

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.semantics.testTagsAsResourceId
import androidx.compose.ui.unit.dp
import com.moniz.ui.loading.Loading
import com.moniz.ui.utils.extensions.conditional

@Composable
fun MScaffold(
    modifier: Modifier = Modifier,
    showLoading: Boolean = false,
    contentScrollState: ScrollState = rememberScrollState(),
    useContentHorizontalPadding: Boolean = false,
    content: @Composable ColumnScope.(ScrollState) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(showLoading) {
        if (showLoading) keyboardController?.hide()
    }

    Scaffold(
        modifier = modifier.semantics { testTagsAsResourceId = true },
        topBar = {
            TopAppBar(
                title = { Text("Simple Scaffold Screen") },
                navigationIcon = {
                    IconButton(onClick = { /* "Open nav drawer" */ }) {
                        Icon(Icons.Filled.Menu, contentDescription = "Localized description")
                    }
                },
            )
        }
    ) {
        Box(
            modifier = Modifier
                .padding(paddingValues = it)
                .fillMaxSize()
                .pointerInput(Unit) {
                    detectTapGestures(onTap = { focusManager.clearFocus() })
                },
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .conditional(
                        predicate = useContentHorizontalPadding,
                        ifTrue = { padding(horizontal = 16.dp) } //TODO: from theme
                    )
                    .verticalScroll(contentScrollState),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                content(contentScrollState)
            }
            if (showLoading) Loading()
        }
    }
}
