package com.moniz.data_db.entities

import androidx.room.Embedded
import com.moniz.data_db.entities.tables.ExerciseEntity


data class ExerciseEntryEntity(
    @Embedded(prefix = "exercise_") val exercise: ExerciseEntity,
    val weight: Float,
    val reps: Int,
    val restPause: Int? = null,
    val peakPause: Int? = null,
    val pauseStretch: Int? = null,
    val dropSet: Int? = null,
)
