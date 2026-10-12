package com.moniz.domain.usecases.exercises

import com.moniz.domain.errors.ErrorMapper
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.repositories.ExerciseRepository
import com.moniz.domain.usecases.base.DataResult
import com.moniz.domain.usecases.base.DataResultFlowUseCase
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ObserveExercisesUseCase @Inject constructor(
    errorMapper: ErrorMapper,
    private val exerciseRepository: ExerciseRepository
) : DataResultFlowUseCase<Unit, List<Exercise>>(errorMapper) {

    override fun buildUseCase(params: Unit): Flow<DataResult<List<Exercise>>> {
        return exerciseRepository.observeExercises()
    }
}
