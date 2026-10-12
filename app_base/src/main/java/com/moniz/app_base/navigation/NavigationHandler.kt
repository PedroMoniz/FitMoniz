package com.moniz.app_base.navigation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
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

    suspend fun navigateTo(screen: AppRoute) {
        emit(Command.Navigate(screen = screen))
    }

    suspend fun replace(screen: AppRoute) {
        emit(Command.Replace(screen = screen))
    }

    suspend fun replaceAll(vararg screens: AppRoute) {
        emit(Command.ReplaceAll(screens = screens.toList()))
    }

    suspend fun pop(untilScreen: KClass<out AppRoute>? = null) {
        emit(Command.Pop(untilScreen = untilScreen))
    }

    suspend fun <T> popWithResult(result: T) {
        emit(Command.PopWithResult(result = result))
    }

    //FIXME: remove all tryEmit usages when old Java screens/views are removed from the project
    private fun tryEmit(command: Command) {
        CoroutineScope(Dispatchers.Main).launch {
            _sharedFlow.emit(command)
        }
    }

    fun tryNavigateTo(screen: AppRoute) {
        tryEmit(Command.Navigate(screen = screen))
    }

    fun tryReplace(screen: AppRoute) {
        tryEmit(Command.Replace(screen = screen))
    }

    fun tryPop() {
        tryEmit(Command.Pop(untilScreen = null))
    }

    sealed class Command {
        data class Navigate(val screen: AppRoute) : Command()
        data class Replace(val screen: AppRoute) : Command()
        data class ReplaceAll(val screens: List<AppRoute>) : Command()
        data class Pop(val untilScreen: KClass<out AppRoute>?) : Command()
        data class PopWithResult<T>(val result: T) : Command()
    }
}
