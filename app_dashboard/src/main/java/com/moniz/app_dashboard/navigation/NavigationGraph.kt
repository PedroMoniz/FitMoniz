package com.moniz.app_dashboard.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.moniz.app_dashboard.dashboard.DashboardScreen
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute

@Serializable
data class DetailRoute(val itemId: String)

fun NavGraphBuilder.dashboardGraph(navController: NavController) {
    composable<DashboardRoute> {
        DashboardScreen(accountId = "test")
    }

    composable<DetailRoute> { backStackEntry ->
        val route = backStackEntry.toRoute<DetailRoute>()
        DashboardScreen(accountId = route.itemId)
    }
}
