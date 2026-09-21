package com.moniz.app_base.utils

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.NonRestartableComposable

@Composable
@NonRestartableComposable
fun EventEffect(
    event: StateEvent,
    onConsumed: () -> Unit,
    action: suspend () -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEvent.Triggered) {
            action()
            onConsumed()
        }
    }
}

@Composable
@NonRestartableComposable
fun DialogEventEffect(
    event: StateEvent,
    action: @Composable () -> Unit
) {
    if (event is StateEvent.Triggered) {
        action()
    }
}

@Composable
@NonRestartableComposable
fun <T : Trigger> EventEffect(
    event: StateEventWithContent<T>,
    onConsumed: () -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEventWithContentTriggered) {
            action(event.content)
            onConsumed()
        }
    }
}

@Composable
@NonRestartableComposable
fun <T : Trigger> EventEffect(
    event: StateEventWithContent<T>,
    onConsumed: (T) -> Unit,
    action: suspend (T) -> Unit
) {
    LaunchedEffect(key1 = event) {
        if (event is StateEventWithContentTriggered) {
            action(event.content)
            onConsumed(event.content)
        }
    }
}

@Composable
@NonRestartableComposable
fun <T : Trigger> DialogEventEffect(
    event: StateEventWithContent<T>,
    action: @Composable (T) -> Unit
) {
    if (event is StateEventWithContentTriggered) {
        action(event.content)
    }
}
