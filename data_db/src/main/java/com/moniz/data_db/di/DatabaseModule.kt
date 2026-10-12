package com.moniz.data_db.di

import android.content.Context
import androidx.room.Room
import com.moniz.data_db.database.MemoryDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideInMemoryDatabase(@ApplicationContext context: Context): MemoryDatabase {
        return Room.inMemoryDatabaseBuilder(
            context,
            MemoryDatabase::class.java
        )
            .fallbackToDestructiveMigration(true)
            .build()
    }

    @Provides
    fun provideExercisesDao(db: MemoryDatabase) = db.exercisesDao()
}
