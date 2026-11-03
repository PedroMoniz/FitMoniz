package com.moniz.data_db.dao.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.moniz.data_db.converters.CommonTypeConverters
import com.moniz.data_db.dao.ExercisesDao
import com.moniz.data_db.entities.ExerciseEntity


@Database(
    entities = [ExerciseEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    value = [CommonTypeConverters::class]
)
abstract class MemoryDatabase : RoomDatabase() {
    abstract fun exercisesDao(): ExercisesDao
}
