package com.moniz.app_base.events

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class EventBus @Inject constructor() {
    private val _events = MutableSharedFlow<Event>(replay = 0)
    val events: SharedFlow<Event>
        get() = _events

    suspend fun notify(event: Event) {
        _events.emit(event)
    }
}
