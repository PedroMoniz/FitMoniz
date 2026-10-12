package com.moniz.data_db.database

import com.moniz.data_db.dao.ExercisesDao
import com.moniz.data_db.entities.ExerciseEntity


@Database(entities = [ExerciseEntity::class], version = 1)
@TypeConverters(CommonTypeConverters::class)
abstract class PersistentDatabase : RoomDatabase() {
    abstract fun exercisesDao(): ExercisesDao
}
