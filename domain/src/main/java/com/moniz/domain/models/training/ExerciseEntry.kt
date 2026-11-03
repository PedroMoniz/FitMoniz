package com.moniz.domain.models.training

import com.moniz.domain.models.DomainModel
import com.moniz.domain.models.exercises.Exercise

data class ExerciseEntry(
    val exercise: Exercise,
    val weight: Float = 0.0f,
    val reps: Int = 0,
    val restPause: Int? = null,
    val peakPause: Int? = null,
    val pauseStretch: Int? = null,
    val dropSet: Int? = null,
) : DomainModel
