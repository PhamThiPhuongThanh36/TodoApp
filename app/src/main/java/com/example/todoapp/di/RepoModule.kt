package com.example.todoapp.di

import com.example.todoapp.database.dao.ListDao
import com.example.todoapp.database.dao.ProjectDao
import com.example.todoapp.database.dao.TaskDao
import com.example.todoapp.repository.ListRepository
import com.example.todoapp.repository.ProjectRepository
import com.example.todoapp.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepoModule {
    @Singleton
    @Provides
    fun provideProjectRepository(projectDao: ProjectDao): ProjectRepository {
        return ProjectRepository(projectDao)
    }

    @Singleton
    @Provides
    fun provideListRepository(listDao: ListDao): ListRepository {
        return ListRepository(listDao)
    }

    @Singleton
    @Provides
    fun provideTaskRepository(taskDao: TaskDao): TaskRepository {
        return TaskRepository(taskDao)
    }


}