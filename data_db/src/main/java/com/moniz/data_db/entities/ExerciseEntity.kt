package com.moniz.data_db.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.UUID

@Entity(
    tableName = "exercises"
)
data class ExerciseEntity(
    @PrimaryKey val id: String = UUID.randomUUID().toString(),
    val title: String,
    val category: Int,
    val weight: Float? = null,
    val reps: Int? = null,
    val restPause: Int? = null,
    val peakPause: Int? = null,
    val pauseStretch: Int? = null,
    val dropSet: Int? = null,
)
