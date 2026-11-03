package com.moniz.domain.models.exercises

import com.moniz.domain.models.DomainModel

data class Exercise(
    val title: String,
    val category: ExerciseCategory,
    val weight: Float? = null,
    val reps: Int? = null,
    val restPause: Int? = null,
    val peakPause: Int? = null,
    val pauseStretch: Int? = null,
    val dropSet: Int? = null,
) : DomainModel
