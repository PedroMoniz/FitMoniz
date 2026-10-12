package com.moniz.repository.mappers

import com.moniz.data_db.entities.ExerciseEntity
import com.moniz.domain.models.exercises.Exercise

internal fun Exercise.toEntity(): ExerciseEntity {
    return ExerciseEntity(
        title = title,
    )
}

internal fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        title = title,
    )
}
