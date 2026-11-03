package com.moniz.app_base.navigation

import androidx.navigation3.runtime.NavKey
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.reflect.KClass

@Singleton
class NavigationHandler @Inject constructor() {
    private val _sharedFlow = MutableSharedFlow<Command>()
    val sharedFlow = _sharedFlow.asSharedFlow()

    private suspend fun emit(command: Command) {
        _sharedFlow.emit(command)
    }

    suspend fun navigateTo(screen: NavKey) {
        emit(Command.Navigate(screen = screen))
    }

    suspend fun replace(screen: NavKey) {
        emit(Command.Replace(screen = screen))
    }

    suspend fun replaceAll(vararg screens: NavKey) {
        emit(Command.ReplaceAll(screens = screens.toList()))
    }

    suspend fun pop(untilScreen: KClass<out NavKey>? = null) {
        emit(Command.Pop(untilScreen = untilScreen))
    }

    suspend fun <T> popWithResult(result: T) {
        emit(Command.PopWithResult(result = result))
    }

    sealed class Command {
        data class Navigate(val screen: NavKey) : Command()
        data class Replace(val screen: NavKey) : Command()
        data class ReplaceAll(val screens: List<NavKey>) : Command()
        data class Pop(val untilScreen: KClass<out NavKey>?) : Command()
        data class PopWithResult<T>(val result: T) : Command()
    }
}
