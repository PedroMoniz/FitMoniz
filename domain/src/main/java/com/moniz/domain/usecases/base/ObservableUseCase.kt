package com.moniz.domain.usecases.base

import io.reactivex.Observable
import io.reactivex.Scheduler

abstract class ObservableUseCase<in Params, Type>(
    private val executionScheduler: Scheduler,
    private val notifyScheduler: Scheduler
) {

    protected abstract fun buildUseCase(params: Params): Observable<Type>

    operator fun invoke(params: Params): Observable<Type> {
        return Observable.just(Unit).flatMap { buildUseCaseWithSchedulers(params) }
    }

    private fun buildUseCaseWithSchedulers(params: Params): Observable<Type> {
        return buildUseCase(params)
            .subscribeOn(executionScheduler)
            .observeOn(notifyScheduler)
    }
}
