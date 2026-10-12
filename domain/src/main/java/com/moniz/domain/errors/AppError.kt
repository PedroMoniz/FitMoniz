package com.moniz.domain.errors

open class AppError(
    override val message: String? = null,
    override val cause: Throwable? = null
) : Exception(message, cause)
