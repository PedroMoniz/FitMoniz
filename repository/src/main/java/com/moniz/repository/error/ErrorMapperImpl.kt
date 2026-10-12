package com.moniz.repository.error

import com.moniz.domain.errors.AppError
import com.moniz.domain.errors.ErrorMapper
import com.moniz.domain.errors.GenericError
import com.moniz.domain.errors.IgnoreError
import timber.log.Timber
import java.net.UnknownHostException
import javax.inject.Inject

class ErrorMapperImpl @Inject constructor() : ErrorMapper {
    override fun map(throwable: Throwable): Throwable {
        Timber.e(throwable, "Error in API")
        return when (throwable) {
            //For special cases where the error has already been mapped.
            is AppError -> throwable

            is UnknownHostException -> {
//                errorConsumer.noInternet()
                IgnoreError()
            }

            else -> {
                Timber.e(throwable, "ErrorMapperImpl: Unmapped error")
                GenericError()
            }
        }
    }
}
