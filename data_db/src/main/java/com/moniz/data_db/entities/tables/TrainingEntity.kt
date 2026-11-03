package com.moniz.data_db.entities.tables

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.moniz.data_db.entities.ExerciseEntryEntity
import java.time.LocalDate
import java.util.UUID

@Entity(tableName = "trainings")
data class TrainingEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val startingDay: LocalDate,
    @Embedded(prefix = "monday_") val mondayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "tuesday_") val tuesdayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "wednesday_") val wednesdayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "thursday_") val thursdayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "friday_") val fridayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "saturday_") val saturdayEntry: ExerciseEntryEntity?,
    @Embedded(prefix = "sunday_") val sundayEntry: ExerciseEntryEntity?,
)
