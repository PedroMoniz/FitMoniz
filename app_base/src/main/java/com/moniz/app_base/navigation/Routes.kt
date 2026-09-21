package com.moniz.app_base.navigation

import kotlinx.serialization.Serializable

@Serializable
object RootRoute : AppRoute

@Serializable
object ListRoute : AppRoute

@Serializable
data class DetailRoute(val itemId: String) : AppRoute
