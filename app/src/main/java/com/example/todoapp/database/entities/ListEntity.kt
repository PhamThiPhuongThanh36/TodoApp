package com.example.todoapp.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lists")
data class ListEntity(
    @PrimaryKey(autoGenerate = true)
    val listId: Int? = null,
    val projectId: Int,
    val listName: String,
)