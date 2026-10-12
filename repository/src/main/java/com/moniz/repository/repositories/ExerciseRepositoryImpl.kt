package com.moniz.repository.repositories

import com.moniz.data_db.repositories.ExerciseLocalRepository
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.repositories.ExerciseRepository
import com.moniz.domain.usecases.base.DataResult
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class ExerciseRepositoryImpl @Inject constructor(
    private val ExerciseLocalRepository: ExerciseLocalRepository
) : ExerciseRepository {

    override fun observeExercises(): Flow<DataResult<List<Exercise>>> {
        TODO("Not yet implemented")
    }

}
