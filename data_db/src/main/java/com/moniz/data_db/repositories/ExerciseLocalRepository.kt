package com.moniz.data_db.repositories

import com.moniz.data_db.dao.ExercisesDao
import com.moniz.data_db.entities.ExerciseEntity
import javax.inject.Inject
import javax.inject.Provider

class ExerciseLocalRepository @Inject constructor(
    private val dao: Provider<ExercisesDao>
) {

    suspend fun getByTitle(id: String): ExerciseEntity? {
        return dao.get().getByTitle(id)
    }
}
