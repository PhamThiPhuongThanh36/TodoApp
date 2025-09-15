package com.example.todoapp.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.example.todoapp.database.AppDatabase
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
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "todo_database"
        ).build()
    }

    @Singleton
    @Provides
    fun provideProjectDao(database: AppDatabase) = database.projectDao()

    @Singleton
    @Provides
    fun provideListDao(database: AppDatabase) = database.listDao()

    @Singleton
    @Provides
    fun provideTaskDao(database: AppDatabase) = database.taskDao()

}