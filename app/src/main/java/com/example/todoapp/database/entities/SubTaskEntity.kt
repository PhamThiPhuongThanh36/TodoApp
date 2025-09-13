package com.example.todoapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "subtasks")
data class SubTaskEntity(
    @PrimaryKey(autoGenerate = true)
    val subTaskId: Int,
    val taskId: Int,
    val subTaskName: String,
    val status: Boolean
)