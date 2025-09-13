package com.example.todoapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tasktags")
data class TaskTagEntity(
    @PrimaryKey(autoGenerate = true)
    val ttId: Int,
    val taskId: Int,
    val tagId: Int
)