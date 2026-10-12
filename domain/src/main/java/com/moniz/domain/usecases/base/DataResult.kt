package com.moniz.domain.usecases.base

sealed class DataResult<out T> {
    data class Success<out T>(val value: T) : DataResult<T>()
    data class Failure(val throwable: Throwable) : DataResult<Nothing>()
    data object Loading : DataResult<Nothing>()
}
