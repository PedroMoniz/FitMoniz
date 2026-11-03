package com.moniz.fitmoniz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.ContentTransform
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.navigationsuite.NavigationSuiteScaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.runtime.result.rememberResultEventBus
import androidx.navigation3.ui.NavDisplay
import com.moniz.app_base.events.Event
import com.moniz.app_base.events.EventBus
import com.moniz.app_base.navigation.NavigationHandler
import com.moniz.app_base.navigation.NavigationHandler.Command
import com.moniz.app_dashboard.dashboard.DashboardScreen
import com.moniz.app_dashboard.navigation.CatalogRoute
import com.moniz.app_dashboard.navigation.DashboardRoute
import com.moniz.app_dashboard.navigation.dashboardEntryProvider
import com.moniz.ui.theme.FitMonizTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import javax.inject.Inject
import kotlin.reflect.KClass

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var eventBus: EventBus

    @Inject
    lateinit var navigationHandler: NavigationHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FitMonizTheme {
                FitMonizApp(
                    navigationHandler = navigationHandler,
                    eventBus = eventBus
                )
            }
        }
    }
}

@Composable
fun FitMonizApp(
    navigationHandler: NavigationHandler,
    eventBus: EventBus
) {

    HandleEvents(eventBus)


    val backStack = rememberNavBackStack(DashboardRoute)
    val resultEventBus = rememberResultEventBus()
    LaunchedEffect(navigationHandler) {
        navigationHandler.sharedFlow.collectLatest { command ->
            when (command) {
                is Command.Navigate -> {
                    backStack.add(command.screen)
                }
                is Command.Replace -> {
                    backStack.removeLastOrNull()
                    backStack.add(command.screen)
                }
                is Command.ReplaceAll -> {
                    backStack.clear()
                    backStack.addAll(command.screens)
                }
                is Command.Pop -> {
                    if (command.untilScreen == null) {
                        backStack.removeLastOrNull()
                    } else {
                        while (backStack.size > 1 && backStack.last() != command.untilScreen) {
                            backStack.removeLastOrNull()
                        }
                    }
                }
                is Command.PopWithResult<*> -> {
                    val result = command.result!!
                    resultEventBus.sendResult(result::class.toString(), result)
                    backStack.removeLastOrNull()
                }
            }
        }
    }

    NavigationSuiteScaffold(
        navigationSuiteItems = {
            AppDestinations.entries.forEach { destination ->
                val isSelected =
                    backStack.lastOrNull()?.let { destination.routeClass.isInstance(it) } == true
                item(
                    icon = {
                        Icon(
                            imageVector = if (isSelected) destination.iconFilled else destination.iconOutline,
                            contentDescription = destination.label
                        )
                    },
                    label = { Text(destination.label) },
                    selected = isSelected,
                    onClick = {
                        val existingIndex =
                            backStack.indexOfFirst { destination.routeClass.isInstance(it) }
                        if (existingIndex != -1) {
                            while (backStack.size > existingIndex + 1) {
                                backStack.removeLastOrNull()
                            }
                        } else {
                            backStack.clear()
                            backStack.add(destination.route)
                        }
                    }
                )
            }
        }
    ) {
        NavDisplay(
            backStack = backStack,
            transitionSpec = {
                ContentTransform(EnterTransition.None, ExitTransition.None)
            },
            popTransitionSpec = {
                ContentTransform(EnterTransition.None, ExitTransition.None)
            },
            predictivePopTransitionSpec = {
                ContentTransform(EnterTransition.None, ExitTransition.None)
            },
            onBack = { backStack.removeLastOrNull() },
            entryDecorators = listOf(
                rememberSaveableStateHolderNavEntryDecorator(),
                rememberViewModelStoreNavEntryDecorator()
            ),
            entryProvider = { key ->
                dashboardEntryProvider(key)
                    ?: NavEntry(key) { DashboardScreen() }
            }
        )
    }
}


@Composable
private fun HandleEvents(eventBus: EventBus) {
    var errorMessage by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(eventBus) {
        eventBus.events.collectLatest { event ->
            when (event) {
                is Event.ErrorEvent.Text -> {
                    errorMessage = event.message
                }
                is Event.ErrorEvent.Error -> {
                    errorMessage = event.error.localizedMessage ?: "An unknown error occurred"
                }
                is Event.ErrorEvent.Resource -> {
//TODO, for demo purposes, lets skip this
                }
                is Event.Snackbar -> TODO()
            }
        }
    }

    if (errorMessage != null) {
        AlertDialog(
            onDismissRequest = { errorMessage = null },
            title = { Text("Error") },
            text = { Text(errorMessage.orEmpty()) },
            confirmButton = {
                TextButton(onClick = { errorMessage = null }) {
                    Text("OK")
                }
            }
        )
    }
}


enum class AppDestinations(
    val label: String,
    val iconFilled: ImageVector,
    val iconOutline: ImageVector,
    val route: NavKey,
    val routeClass: KClass<*>
) {
    DASHBOARD(
        label = "Dashboard",
        iconFilled = Icons.Filled.Home,
        iconOutline = Icons.Outlined.Home,
        route = DashboardRoute,
        routeClass = DashboardRoute::class
    ),
    CATALOG(
        label = "Catalog",
        iconFilled = Icons.Filled.Search,
        iconOutline = Icons.Outlined.Search,
        route = CatalogRoute,
        routeClass = CatalogRoute::class
    ),
}
