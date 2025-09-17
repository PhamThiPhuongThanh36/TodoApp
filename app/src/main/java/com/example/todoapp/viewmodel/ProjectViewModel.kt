package com.example.todoapp.viewmodel

import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.entities.ProjectEntity
import com.example.todoapp.helper.DataStoreHelper
import com.example.todoapp.repository.ProjectRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ProjectViewModel @Inject constructor(private val repository: ProjectRepository) : ViewModel() {
    private val _projects = MutableStateFlow<List<ProjectEntity>>(emptyList())
    val projects: StateFlow<List<ProjectEntity>> = _projects

    init {
        viewModelScope.launch {
            repository.getAllProjects().collect { projectList ->
                _projects.value = projectList
            }
        }
    }

    fun insertProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.insertProject(project)
        }
    }

    fun updateProject(project: ProjectEntity) {
        viewModelScope.launch {
            repository.updateProject(project)
        }
    }

    fun deleteProject(projectId: Int) {
        viewModelScope.launch {
            repository.deleteProject(projectId)
        }
    }

    fun getProjectById(projectId: Int): Flow<ProjectEntity?> {
        return repository.getProjectById(projectId)
    }
}