package com.moniz.fitmoniz.di

import com.moniz.repository.di.RepositoryModule
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@InstallIn(SingletonComponent::class)
@Module(
    includes = [
        // Dependencies modules
        RepositoryModule::class
    ]
)
interface AppAggregatorModule
