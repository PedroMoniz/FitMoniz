package com.moniz.app_base.utils

import androidx.compose.runtime.Stable

@Stable
sealed interface UiState<out T> {

    @Stable
    data object Loading : UiState<Nothing>

    @Stable
    data class Success<T>(val data: T) : UiState<T>

    @Stable
    data class Error(val throwable: Throwable? = null, val message: String? = null) : UiState<Nothing>

    @Stable
    data object Empty : UiState<Nothing>
}
