package com.moniz.domain.usecases.base

import io.reactivex.Maybe
import io.reactivex.Scheduler
import io.reactivex.Single

abstract class MaybeUseCase<in Params, Type>(
    private val executionScheduler: Scheduler,
    private val notifyScheduler: Scheduler
) {

    protected abstract fun buildUseCase(params: Params): Maybe<Type>

    operator fun invoke(params: Params): Maybe<Type> {
        return Single.just(Unit).flatMapMaybe { buildUseCaseWithSchedulers(params) }
    }

    private fun buildUseCaseWithSchedulers(params: Params): Maybe<Type> {
        return buildUseCase(params)
            .subscribeOn(executionScheduler)
            .observeOn(notifyScheduler)
    }
}
