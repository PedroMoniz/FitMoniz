package com.moniz.repository.di.modules

import com.moniz.domain.errors.ErrorMapper
import com.moniz.repository.error.ErrorMapperImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class ImplementationsModule {

    @Binds
    abstract fun bindErrorMapper(challengeProvider: ErrorMapperImpl): ErrorMapper

}
