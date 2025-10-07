package com.example.todoapp.database.dao

import android.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity
import com.example.todoapp.database.entities.TaskWithTags
import com.example.todoapp.model.TaskWithListAndProject
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity) : Long

    @Query("SELECT * FROM tasks WHERE listId = :listId AND statusDelete = 0 ORDER BY status, createdAt DESC")
    fun getTasksByListId(listId: Int): Flow<List<TaskEntity>>

    @Query("SELECT * FROM tasks WHERE statusDelete = 1")
    fun getDeletedTasks(): Flow<List<TaskEntity>>

    @Query("UPDATE tasks SET statusDelete = 1 WHERE taskId = :taskId")
    suspend fun insertDeletedTask(taskId: Int)

    @Query ("UPDATE tasks SET statusDelete = 0 WHERE taskId = :taskId")
    suspend fun restoreTask(taskId: Int)

    @Query("DELETE FROM tasks WHERE taskId = :taskId")
    suspend fun deleteTask(taskId: Int)

    @Query("SELECT * FROM tasks WHERE taskId = :taskId")
    fun getTaskById(taskId: Int): Flow<TaskEntity?>

    @Update
    suspend fun updateTask(task: TaskEntity)

    @Query("UPDATE tasks SET status = :status WHERE taskId = :taskId")
    suspend fun updateTaskStatus(taskId: Int, status: Boolean)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTag(tag: TagEntity)

    @Query("SELECT * FROM tags")
    fun getTags(): Flow<List<TagEntity>>

    @Delete
    suspend fun deleteTag(tag: TagEntity)

    @Transaction
    @Query("SELECT * FROM tasks WHERE listId = :listId AND statusDelete = 0 ORDER BY status, createdAt DESC")
    fun getTasksWithTags(listId: Int): Flow<List<TaskWithTags>>

    @Transaction
    @Query("SELECT * FROM tasks LEFT JOIN tasks_tags ON tasks.taskId = tasks_tags.taskId WHERE tasks.taskId = :taskId")
    fun getTaskWithTagsById(taskId: Int): Flow<TaskWithTags?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskTag(taskTag: TaskTagEntity)

    @Query("DELETE FROM tasks_tags WHERE taskId = :taskId")
    suspend fun deleteTaskTagByTaskId(taskId: Int)

    @Transaction
    @Query("""
        SELECT * FROM tasks
        LEFT JOIN tasks_tags ON tasks.taskId = tasks_tags.taskId
        WHERE tasks_tags.tagId = :tagId ORDER BY tasks.status, tasks.createdAt DESC
    """)
    fun getTasksByTagId(tagId: Int): Flow<List<TaskWithTags>>

    @Query("SELECT * FROM tasks WHERE dueDate IS NOT NULL")
    fun getAllTasks(): Flow<List<TaskEntity>>

    @Transaction
    @Query("""
    SELECT tasks.taskId, tasks.taskName, tasks.listId, 
           lists.listName, lists.projectId, 
           projects.projectName
    FROM tasks
    INNER JOIN lists ON tasks.listId = lists.listId
    INNER JOIN projects ON lists.projectId = projects.projectId
    WHERE tasks.statusDelete = 1
""")
    fun getTasksWithListAndProject(): Flow<List<TaskWithListAndProject>>

}