package com.moniz.app_dashboard.navigation

import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import com.moniz.app_dashboard.catalog.CatalogScreen
import com.moniz.app_dashboard.dashboard.DashboardScreen
import com.moniz.app_dashboard.detail.DetailScreen
import kotlinx.serialization.Serializable

@Serializable
data object DashboardRoute : NavKey

@Serializable
data object CatalogRoute : NavKey

@Serializable
data class DetailRoute(val itemId: String) : NavKey

fun dashboardEntryProvider(key: Any): NavEntry<NavKey>? {
    return when (key) {
        is DashboardRoute -> NavEntry(key) { DashboardScreen() }
        is CatalogRoute -> NavEntry(key) { CatalogScreen() }
        is DetailRoute -> NavEntry(key) { DetailScreen(id = key.itemId) }
        else -> null
    }
}
