package com.moniz.app_base.utils

import androidx.compose.runtime.Immutable

@Immutable
sealed interface StateEvent {

    @Immutable
    data object Triggered : StateEvent {
        override fun toString(): String = "triggered"
    }

    @Immutable
    data object Consumed : StateEvent {
        override fun toString(): String = "consumed"
    }
}

val triggered = StateEvent.Triggered
val consumed = StateEvent.Consumed
