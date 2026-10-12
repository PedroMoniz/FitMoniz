package com.moniz.repository.di

import com.moniz.repository.di.modules.ImplementationsModule
import com.moniz.repository.di.modules.LocalRepositoriesModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(
    includes = [
        ImplementationsModule::class,
        LocalRepositoriesModule::class,
    ]
)
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule
