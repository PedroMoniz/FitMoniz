package com.moniz.app_base.events


sealed class Event {

    data object DeviceRooted : Event()

    data object UserBlocked : Event()

    data object Navigation : Event()

    data class RatingSheet(val precedingAppFlow: String) : Event()

    data class Snackbar(
        val text: String, //StringResource,
        val action: Action? = null,
        val hasAutoDismiss: Boolean = true,
        val config: Config
    ) : Event() {
        data class Action(
            val text: String, //StringResource,
            val onClick: (() -> Unit)
        )

        sealed class Config {
            data object Success : Config()
            data object Warning : Config()
            data object Danger : Config()
            data object Info : Config()
        }
    }

    sealed class ErrorEvent : Event() {
        data class Error(val error: Throwable) : ErrorEvent()
        data class Resource(val titleResId: Int? = null, val descriptionResId: Int) : ErrorEvent()
        data class Text(val message: String?) : ErrorEvent()
    }

    data object NoInternet : Event()
}
