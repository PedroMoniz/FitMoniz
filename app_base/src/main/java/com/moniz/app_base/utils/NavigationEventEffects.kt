package com.moniz.app_base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun NavigationEventEffect(
    event: StateEvent,
    onConsumed: () -> Unit,
    action: suspend () -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEvent.Triggered) {
            onConsumed()
            action()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T : Trigger> NavigationEventEffect(
    event: StateEventWithContent<T>,
    onConsumed: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEventWithContentTriggered) {
            onConsumed()
            action(event.content)
        }
    }
}
