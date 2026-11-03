package com.moniz.repository.mappers

import com.moniz.data_db.entities.ExerciseEntryEntity
import com.moniz.domain.models.training.ExerciseEntry

fun ExerciseEntry.toEntity(): ExerciseEntryEntity {
    return ExerciseEntryEntity(
        exercise = this.exercise.toEntity(),
        weight = this.weight,
        reps = this.reps,
        restPause = this.restPause,
        peakPause = this.peakPause,
        pauseStretch = this.pauseStretch,
        dropSet = this.dropSet
    )
}

fun ExerciseEntryEntity.toDomain(): ExerciseEntry {
    return ExerciseEntry(
        exercise = this.exercise.toDomain(),
        weight = this.weight,
        reps = this.reps,
        restPause = this.restPause,
        peakPause = this.peakPause,
        pauseStretch = this.pauseStretch,
        dropSet = this.dropSet
    )
}
