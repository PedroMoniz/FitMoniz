package com.moniz.domain.usecases.base

import com.moniz.domain.errors.ErrorMapper
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

abstract class SuspendedUseCase<in Params, Type>(
    private val errorMapper: ErrorMapper,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) {
    protected abstract suspend fun buildUseCase(params: Params): Type
    suspend operator fun invoke(
        params: Params
    ): Result<Type> = withContext(dispatcher) {
        try {
            Result.success(buildUseCase(params))
        } catch (e: Throwable) {
            Result.failure(errorMapper.map(e))
        }
    }
}
