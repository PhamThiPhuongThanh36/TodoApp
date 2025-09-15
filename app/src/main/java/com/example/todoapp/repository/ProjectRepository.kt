package com.example.todoapp.repository

import com.example.todoapp.database.dao.ProjectDao
import com.example.todoapp.database.entities.ProjectEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ProjectRepository @Inject constructor(
    private val projectDao: ProjectDao
) {
    suspend fun insertProject(project: ProjectEntity) {
        projectDao.insertProject(project)
    }
    suspend fun updateProject(project: ProjectEntity) {
        projectDao.updateProject(project)
    }
    fun getAllProjects(): Flow<List<ProjectEntity>> {
        return projectDao.getAllProjects()
    }
    suspend fun deleteProject(projectId: Int) {
        projectDao.deleteProject(projectId)
    }

    fun getProjectById(projectId: Int): Flow<ProjectEntity> {
        return projectDao.getProjectById(projectId)
    }
}