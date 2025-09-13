package com.example.todoapp.database.entities

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tags")
data class TagEntity(
    @PrimaryKey(autoGenerate = true)
    val tagId: Int,
    val tagName: String,
    val tagColor: Int
)