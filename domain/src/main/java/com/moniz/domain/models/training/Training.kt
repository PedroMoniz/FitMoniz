package com.moniz.domain.models.training

import com.moniz.domain.models.DomainModel
import java.time.LocalDate

data class Training(
    val title: String,
    val startingDay: LocalDate,
    val mondayEntry: ExerciseEntry?,
    val tuesdayEntry: ExerciseEntry?,
    val wednesdayEntry: ExerciseEntry?,
    val thursdayEntry: ExerciseEntry?,
    val fridayEntry: ExerciseEntry?,
    val saturdayEntry: ExerciseEntry?,
    val sundayEntry: ExerciseEntry?,
) : DomainModel
