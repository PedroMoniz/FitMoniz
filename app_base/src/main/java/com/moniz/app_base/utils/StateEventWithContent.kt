package com.moniz.app_base.utils

import android.os.Parcelable
import androidx.compose.runtime.Immutable
import com.moniz.app_base.events.Event

/**
 *  This [StateEventWithContent] can have exactly 2 states like the [StateEvent] but the triggered state holds a value of type [T].
 */
@Immutable
sealed interface StateEventWithContent<out Trigger>

/**
 * The event in its triggered state holding a value of [T]. See [triggered] to create an instance of this.
 * @param content A value that is needed on the event consumer side.
 */
@Immutable
data class StateEventWithContentTriggered<T : Trigger>(val content: T) :
    StateEventWithContent<T> {
    override fun toString(): String = "triggered($content)"
}

/**
 * The event in its consumed state not holding any value. See [consumed] to create an instance of this.
 */
@Immutable
object StateEventWithContentConsumed : StateEventWithContent<Nothing> {
    override fun toString(): String = "consumed"
}

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its triggered state holding a value of [T].
 * @param content A value that is needed on the event consumer side.
 */
fun <T : Trigger> triggered(content: T) = StateEventWithContentTriggered(content)

/**
 * A shorter and more readable way to create an [StateEventWithContent] in its consumed state.
 */
fun consumed() = StateEventWithContentConsumed

/**
 * A Wrapper because default String is not Parcelable
 */

sealed class Trigger {

    data class Text(val value: String) : Trigger()
    data class Number(val value: Int) : Trigger()
    data class Bool(val value: Boolean) : Trigger()
    data class Type<T : Event>(val value: T) : Trigger()
}
