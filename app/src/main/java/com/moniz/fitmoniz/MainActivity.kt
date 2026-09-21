package com.moniz.fitmoniz

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation3.runtime.result.rememberResultEventBus
import com.moniz.app_base.navigation.NavigationHandler
import com.moniz.app_base.navigation.RootRoute
import com.moniz.fitmoniz.ui.theme.FitMonizTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            FitMonizTheme {

                val navController = rememberNavController()
                val viewModel: MainViewModel = hiltViewModel()
                FitMonizApp(navController, viewModel)
            }
        }
    }
}

@Composable
fun FitMonizApp(
    navController: NavHostController,
    viewModel: MainViewModel
) {
    val resultEventBus = rememberResultEventBus()
    LaunchedEffect(viewModel) {
        viewModel.navigationHandler.sharedFlow.collectLatest { command ->
            when (command) {
                is NavigationHandler.Command.Navigate -> navController.navigate(command.screen)
                is NavigationHandler.Command.Pop -> {
                    val untilScreen = command.untilScreen
                    if (untilScreen == null) {
                        navController.popBackStack()
                    } else {
                        navController.popBackStack()
                    }
                }
                is NavigationHandler.Command.PopWithResult<*> -> {
                    resultEventBus.sendResult(command.result)
                    navController.popBackStack()
                }
                is NavigationHandler.Command.Replace -> {
                    val currentRoute = navController.currentBackStackEntry?.destination?.route
                        ?: return@collectLatest

                    navController.navigate(command.screen) {
                        popUpTo(currentRoute) { inclusive = true }
                    }
                }
                is NavigationHandler.Command.ReplaceAll -> {
                    if (command.screens.isNotEmpty()) {
                        navController.navigate(command.screens.first()) {
                            popUpTo(navController.graph.id) {
                                inclusive = true
                            }
                        }
                        for (screen in command.screens.drop(1)) {
                            navController.navigate(screen)
                        }
                    }
                }
            }
        }
    }

    NavHost(
        navController = navController,
        startDestination = RootRoute
    ) {
        // Feature module nav graphs plug in here
    }
}
