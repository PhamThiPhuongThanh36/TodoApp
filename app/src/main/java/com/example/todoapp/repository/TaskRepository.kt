package com.example.todoapp.repository

import com.example.todoapp.database.dao.TaskDao
import com.example.todoapp.database.entities.TaskEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun insertTask(task: TaskEntity) {
        taskDao.insertTask(task)
    }

    fun getTasksByListId(listId: Int): Flow<List<TaskEntity>> {
        return taskDao.getTasksByListId(listId)
    }

    suspend fun deleteTask(taskId: Int) {
        taskDao.deleteTask(taskId)
    }

    suspend fun updateTask(task: TaskEntity) {
        taskDao.updateTask(task)
    }

}