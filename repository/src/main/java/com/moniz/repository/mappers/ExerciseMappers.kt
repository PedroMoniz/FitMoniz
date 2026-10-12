package com.moniz.repository.mappers

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
