package com.moniz.domain.errors

interface ErrorMapper {
    fun map(throwable: Throwable): Throwable
}
