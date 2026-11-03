package com.moniz.repository.mappers

import com.moniz.data_db.entities.tables.ExerciseEntity
import com.moniz.domain.models.exercises.Exercise
import com.moniz.domain.models.exercises.ExerciseCategory

internal fun Exercise.toEntity(): ExerciseEntity {
    val categoryValue = category.ordinal
    return ExerciseEntity(
        title = this.title,
        category = categoryValue,
    )
}

internal fun ExerciseEntity.toDomain(): Exercise {
    return Exercise(
        title = this.title,
        category = ExerciseCategory.entries[this.category],
    )
}
