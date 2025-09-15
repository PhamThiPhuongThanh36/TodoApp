package com.example.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ProjectDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProject(project: ProjectEntity)

    @Update
    suspend fun updateProject(project: ProjectEntity)

    @Query("SELECT * FROM projects")
    fun getAllProjects(): Flow<List<ProjectEntity>>

    @Query("DELETE FROM projects WHERE projectId = :projectId")
    suspend fun deleteProject(projectId: Int)

    @Query("SELECT * FROM projects WHERE projectId = :projectId")
    fun getProjectById(projectId: Int): Flow<ProjectEntity>

}