package com.moniz.domain.usecases.base

import com.moniz.domain.errors.ErrorMapper
import com.moniz.domain.errors.IgnoreError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import timber.log.Timber

abstract class DataResultFlowUseCase<in Params, Type>(private val errorMapper: ErrorMapper) {

    protected abstract fun buildUseCase(params: Params): Flow<DataResult<Type>>

    operator fun invoke(params: Params): Flow<DataResult<Type>> =
        flow {
            buildUseCase(params)
                .catch { throwable ->
                    emit(DataResult.Failure(errorMapper.map(throwable)))
                }
                .collect { result ->
                    emit(
                        when (result) {
                            is DataResult.Failure -> DataResult.Failure(errorMapper.map(result.throwable))
                            else -> result
                        }
                    )
                }
        }.catch { throwable ->
            Timber.e(throwable, "Broken Flow: Unmapped error")
            emit(DataResult.Failure(IgnoreError()))
        }
}
