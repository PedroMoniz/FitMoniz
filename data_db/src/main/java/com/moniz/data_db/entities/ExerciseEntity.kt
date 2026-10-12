package com.moniz.data_db.entities

import androidx.room.Entity

@Entity(
    tableName = "exercises",
    primaryKeys = ["id"]
)
data class ExerciseEntity(
    val id: String,
    val title: String
)
