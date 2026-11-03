package com.moniz.repository.repositories

import com.moniz.data_db.repositories.ExerciseLocalRepository
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.repositories.ExerciseRepository
import com.moniz.domain.usecases.base.DataResult
import com.moniz.repository.mappers.toDomain
import com.moniz.repository.mappers.toEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class ExerciseRepositoryImpl @Inject constructor(
    private val exerciseLocalRepository: ExerciseLocalRepository
) : ExerciseRepository {

    override fun observeExercises(): Flow<DataResult<List<Exercise>>> = flow {
        emitAll(
            exerciseLocalRepository
                .observeExercises()
                .map { entities -> entities.map { it.toDomain() } }
                .map { DataResult.Success(it) }
        )
    }

    override suspend fun insertExercises(list: List<Exercise>) {
        exerciseLocalRepository.insertExercises(list.map { it.toEntity() })
    }
}
