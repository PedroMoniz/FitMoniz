package com.moniz.data_db.dao

import androidx.room.Dao
import androidx.room.Query
import com.moniz.data_db.entities.ExerciseEntity

@Dao
interface ExercisesDao {

    @Query("SELECT * FROM exercises WHERE title = :title LIMIT 1")
    suspend fun getByTitle(title: String): ExerciseEntity?

}
