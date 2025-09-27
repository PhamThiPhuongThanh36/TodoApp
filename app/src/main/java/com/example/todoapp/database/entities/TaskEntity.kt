package com.example.todoapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int? = null,
    val listId: Int? = null,
    val taskName: String,
    val status: Boolean = false,
    val note: String? = null,
    val statusDelete: Boolean = false,
    val createdAt: String? = null,
    val dueDate: String? = null
)