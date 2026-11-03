package com.moniz.domain.models.exercises

import com.moniz.domain.models.DomainModel

data class Exercise(
    val title: String,
    val category: ExerciseCategory,
) : DomainModel
