package com.example.todoapp.repository

import com.example.todoapp.database.dao.TaskDao
import com.example.todoapp.database.entities.DeletedTaskEntity
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity
import com.example.todoapp.database.entities.TaskWithTags
import com.example.todoapp.model.TaskWithListAndProject
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TaskRepository @Inject constructor(
    private val taskDao: TaskDao
) {
    suspend fun insertTask(task: TaskEntity): Long {
        return taskDao.insertTask(task)
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

    suspend fun updateTaskStatus(taskId: Int, status: Boolean) {
        taskDao.updateTaskStatus(taskId, status)
    }

    fun getTaskById(taskId: Int): Flow<TaskEntity?> {
        return taskDao.getTaskById(taskId)
    }

    suspend fun insertTag(tag: TagEntity) {
        taskDao.insertTag(tag)
    }

    fun getTags(): Flow<List<TagEntity>> {
        return taskDao.getTags()
    }

    fun getTasksWithTags(listId: Int): Flow<List<TaskWithTags>> {
        return taskDao.getTasksWithTags(listId)
    }

    fun getTaskWithTagsById(taskId: Int): Flow<TaskWithTags?> {
        return taskDao.getTaskWithTagsById(taskId)
    }

    suspend fun insertTaskTag(taskTag: TaskTagEntity) {
        taskDao.insertTaskTag(taskTag)
    }

    suspend fun deleteTaskTagByTaskId(taskId: Int) {
        taskDao.deleteTaskTagByTaskId(taskId)
    }

    fun getTasksByTagId(tagId: Int): Flow<List<TaskWithTags?>> {
        return taskDao.getTasksByTagId(tagId)
    }
    
    fun getAllTasks(): Flow<List<TaskEntity>> {
        return taskDao.getAllTasks()
    }

    suspend fun insertDeletedTask(deletedTask: DeletedTaskEntity) {
        taskDao.insertDeletedTask(deletedTask)
    }

    suspend fun restoreTask(taskId: Int) {
        taskDao.restoreTask(taskId)
    }

    fun getTasksWithListAndProject(): Flow<List<TaskWithListAndProject>> {
        return taskDao.getTasksWithListAndProject()
    }
    
    fun getDeletedTaskById(taskId: Int): Flow<DeletedTaskEntity?> {
        return taskDao.getDeletedTaskById(taskId)
        }
}