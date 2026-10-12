package com.moniz.domain.repositories

import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.usecases.base.DataResult
import kotlinx.coroutines.flow.Flow

interface ExerciseRepository {
    fun observeExercises(): Flow<DataResult<List<Exercise>>>
}
