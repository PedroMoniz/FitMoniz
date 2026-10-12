package com.moniz.repository.di.modules

import com.moniz.domain.repositories.ExerciseRepository
import com.moniz.repository.repositories.ExerciseRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal abstract class LocalRepositoriesModule {

    @Binds
    @Singleton
    abstract fun bindExercisesRepository(repository: ExerciseRepositoryImpl): ExerciseRepository
}
