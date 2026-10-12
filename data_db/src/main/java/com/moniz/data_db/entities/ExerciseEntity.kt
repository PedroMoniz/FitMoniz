package com.moniz.data_db.entities

import androidx.room.Entity
import java.util.UUID

@Entity(
    tableName = "exercises",
    primaryKeys = ["id"]
)
data class ExerciseEntity(
    val id: String = UUID.randomUUID().toString(),
    val title: String
)
