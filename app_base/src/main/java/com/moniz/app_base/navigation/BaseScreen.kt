package com.moniz.app_base.navigation

import androidx.compose.runtime.Composable

interface BaseScreen {

    @Composable
    fun Content()

    val key: String
        get() = this::class.qualifiedName ?: this::class.java.name

    val useStatusBarAlternateColor: Boolean
        get() = true
    val useNavigationBarAlternateColor: Boolean
        get() = true
}
