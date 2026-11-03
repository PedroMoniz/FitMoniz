package com.moniz.data_db.repositories

import com.moniz.data_db.dao.ExercisesDao
import com.moniz.data_db.entities.ExerciseEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Provider

class ExerciseLocalRepository @Inject constructor(
    private val dao: Provider<ExercisesDao>
) {
    suspend fun insertExercises(movies: List<ExerciseEntity>) {
        return dao.get().insertAll(movies)
    }

    fun observeExercises(): Flow<List<ExerciseEntity>> {
        return dao.get().observeAll()
    }

    suspend fun getByTitle(title: String): ExerciseEntity? {
        return dao.get().getByTitle(title)
    }
}
