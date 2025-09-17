package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity
import com.example.todoapp.database.entities.TaskWithTags
import com.example.todoapp.repository.TaskRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TaskViewModel @Inject constructor(
    private val repository: TaskRepository
): ViewModel() {
    fun getTasksByListId(listId: Int): Flow<List<TaskEntity>> {
        return repository.getTasksByListId(listId)
    }

    suspend fun insertTask(task: TaskEntity): Long {
        return repository.insertTask(task)
    }

    fun deleteTask(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTask(taskId)
        }
    }

    fun updateTask(task: TaskEntity) {
        viewModelScope.launch {
            repository.updateTask(task)
        }
    }

    fun updateTaskStatus(taskId: Int, status: Boolean) {
        viewModelScope.launch {
            repository.updateTaskStatus(taskId, status)
        }
    }

    fun getTaskById(taskId: Int): Flow<TaskEntity?> {
        return repository.getTaskById(taskId)
    }

    fun insertTag(tag: TagEntity) {
        viewModelScope.launch {
            repository.insertTag(tag)
        }
    }

    fun getTags(): Flow<List<TagEntity>> {
        return repository.getTags()
    }

    fun getTaskWithTags(listId: Int): Flow<List<TaskWithTags>> {
        return repository.getTasksWithTags(listId)
    }

    fun insertTaskTag(taskTag: TaskTagEntity) {
        viewModelScope.launch {
            repository.insertTaskTag(taskTag)
        }
    }

    fun deleteTaskTagByTaskId(taskId: Int) {
        viewModelScope.launch {
            repository.deleteTaskTagByTaskId(taskId)
        }
    }

    fun getTasksByTagId(tagId: Int): Flow<List<TaskWithTags?>> {
        return repository.getTasksByTagId(tagId)
    }

    fun getTaskWithTagsById(taskId: Int): Flow<TaskWithTags?> {
        return repository.getTaskWithTagsById(taskId)
    }
}