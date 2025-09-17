package com.example.todoapp.database.dao

import android.adservices.adid.AdId
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity
import com.example.todoapp.database.entities.TaskWithTags
import kotlinx.coroutines.flow.Flow

@Dao
interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(task: TaskEntity) : Long

    @Query("SELECT * FROM tasks WHERE listId = :listId")
    fun getTasksByListId(listId: Int): Flow<List<TaskEntity>>

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

    @Transaction
    @Query("SELECT * FROM tasks WHERE listId = :listId")
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
        WHERE tasks_tags.tagId = :tagId
    """)
    fun getTasksByTagId(tagId: Int): Flow<List<TaskWithTags>>


}