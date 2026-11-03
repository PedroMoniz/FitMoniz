package com.moniz.domain.utils

import com.moniz.domain.usecases.base.DataResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map

fun <T> T.toDataResult(): DataResult<T> {
    return if (this is Throwable) DataResult.Failure(this) else DataResult.Success(this)
}

inline fun <T> DataResult<T>.onSuccess(action: (value: T) -> Unit): DataResult<T> {
    if (this is DataResult.Success) action(value)
    return this
}

inline fun <T> DataResult<T>.onFailure(action: (throwable: Throwable) -> Unit): DataResult<T> {
    if (this is DataResult.Failure) action(throwable)
    return this
}

@SinceKotlin("1.3")
inline fun <R, T> DataResult<T>.fold(
    onSuccess: (value: T) -> R,
    onFailure: (throwable: Throwable) -> R,
    onLoading: () -> R
): R {
    return when (this) {
        is DataResult.Success -> onSuccess(value)
        is DataResult.Failure -> onFailure(throwable)
        is DataResult.Loading -> onLoading()
    }
}

inline fun <T1, T2, R> fold(
    result1: DataResult<T1>,
    result2: DataResult<T2>,
    onSuccess: (T1, T2) -> R,
    onFailure: (throwable: Throwable) -> R,
    onLoading: () -> R
): R {
    return when {
        result1 is DataResult.Loading || result2 is DataResult.Loading -> onLoading()
        result1 is DataResult.Failure -> onFailure(result1.throwable)
        result2 is DataResult.Failure -> onFailure(result2.throwable)
        else -> onSuccess(
            (result1 as DataResult.Success).value,
            (result2 as DataResult.Success).value
        )
    }
}

inline fun <T1, T2, T3, R> fold(
    result1: DataResult<T1>,
    result2: DataResult<T2>,
    result3: DataResult<T3>,
    onSuccess: (T1, T2, T3) -> R,
    onFailure: (throwable: Throwable) -> R,
    onLoading: () -> R
): R {
    return when {
        result1 is DataResult.Loading || result2 is DataResult.Loading || result3 is DataResult.Loading -> onLoading()
        result1 is DataResult.Failure -> onFailure(result1.throwable)
        result2 is DataResult.Failure -> onFailure(result2.throwable)
        result3 is DataResult.Failure -> onFailure(result3.throwable)
        else -> onSuccess(
            (result1 as DataResult.Success).value,
            (result2 as DataResult.Success).value,
            (result3 as DataResult.Success).value
        )
    }
}

fun <T1, T2, R> combineFold(
    flow1: Flow<DataResult<T1>>,
    flow2: Flow<DataResult<T2>>,
    onSuccess: (T1, T2) -> R,
    onFailure: (Throwable) -> R,
    onLoading: () -> R
): Flow<R> = combine(flow1, flow2) { result1, result2 ->
    fold(result1, result2, onSuccess = onSuccess, onFailure = onFailure, onLoading = onLoading)
}

fun <T1, T2, T3, R> combineFold(
    flow1: Flow<DataResult<T1>>,
    flow2: Flow<DataResult<T2>>,
    flow3: Flow<DataResult<T3>>,
    onSuccess: (T1, T2, T3) -> R,
    onFailure: (Throwable) -> R,
    onLoading: () -> R
): Flow<R> = combine(flow1, flow2, flow3) { result1, result2, result3 ->
    fold(
        result1,
        result2,
        result3,
        onSuccess = onSuccess,
        onFailure = onFailure,
        onLoading = onLoading
    )
}

suspend inline fun <R, T> Flow<DataResult<T>>.getFirstOnSuccess(crossinline onSuccess: (value: T) -> R?): R? {
    return this
        .filterIsInstance<DataResult.Success<T>>()
        .map { result -> onSuccess(result.value) }
        .firstOrNull()
}

fun <T, R> Flow<DataResult<T>>.mapOnSuccess(transform: (value: T) -> R): Flow<DataResult<R>> {
    return this.map { result ->
        when (result) {
            is DataResult.Success -> DataResult.Success(transform(result.value))
            is DataResult.Failure -> result
            is DataResult.Loading -> result
        }
    }
}
