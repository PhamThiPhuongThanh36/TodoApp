package com.example.todoapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasks")
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    val taskId: Int,
    val listId: Int,
    val taskName: String,
    val status: Boolean,
    val note: String
)