package com.moniz.domain.usecases.base

interface SynchronousUseCase<in Params, Type> {
    operator fun invoke(params: Params): Type
}
