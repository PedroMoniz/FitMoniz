package com.moniz.ui.utils.extensions

import androidx.compose.runtime.Stable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.semantics.clearAndSetSemantics

@Stable
fun Modifier.setEnabledBehavior(isEnabled: Boolean = true) =
    this.conditional(isEnabled, ifTrue = { this }, ifFalse = { alpha(0.5F) })

@Stable
fun Modifier.setInvisible(invisible: Boolean = true): Modifier =
    this.conditional(invisible, ifTrue = { alpha(0f).clearAndSetSemantics {} })

@Stable
inline fun Modifier.conditional(
    predicate: Boolean,
    ifTrue: Modifier.() -> Modifier,
    ifFalse: Modifier.() -> Modifier = { this }
): Modifier =
    if (predicate) {
        ifTrue(this)
    } else {
        ifFalse(this)
    }

@Stable
inline fun <T> Modifier.nullConditional(
    argument: T?,
    ifNotNull: Modifier.(T) -> Modifier,
    ifNull: Modifier.() -> Modifier = { this }
): Modifier =
    if (argument != null) {
        ifNotNull(this, argument)
    } else {
        ifNull(this)
    }
