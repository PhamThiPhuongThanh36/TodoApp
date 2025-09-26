package com.example.todoapp.model

data class List(
    val listName: String
)

data class TaskWithListAndProject(
    val taskId: Int?,
    val taskName: String,
    val listId: Int?,
    val listName: String,
    val projectId: Int?,
    val projectName: String
)