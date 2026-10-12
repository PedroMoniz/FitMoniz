package com.moniz.domain.usecases.base

import com.moniz.domain.errors.ErrorMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class FlowUseCase<in Params, Type>(private val errorMapper: ErrorMapper) {

    protected abstract fun buildUseCase(params: Params): Flow<Type>

    operator fun invoke(params: Params): Flow<Result<Type>> =
        flow {
            buildUseCase(params)
                .catch { throwable ->
                    emit(Result.failure(errorMapper.map(throwable)))
                }
                .collect { value ->
                    emit(Result.success(value))
                }
        }.catch { throwable ->
            Timber.e(throwable, "Broken Flow: Unmapped error")
            emit(Result.failure(IgnoreError()))
        }
}
