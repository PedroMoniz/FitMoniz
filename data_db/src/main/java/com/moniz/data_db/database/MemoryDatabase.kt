package com.moniz.data_db.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.moniz.data_db.dao.ExercisesDao
import com.moniz.data_db.entities.ExerciseEntity


@Database(entities = [ExerciseEntity::class], version = 1)
abstract class MemoryDatabase : RoomDatabase() {
    abstract fun exercisesDao(): ExercisesDao
}
