package com.moniz.repository.repositories

import javax.inject.Inject

internal class ExerciseRepositoryImpl @Inject constructor(
    private val ExerciseLocalRepository: ExerciseLocalRepository
) : ExerciseRepository {

    override suspend fun observeExercises() {

    }
}
