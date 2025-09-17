package com.example.todoapp.database.entities

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Junction
import androidx.room.Relation

@Entity(
    tableName = "tasks_tags",
    primaryKeys = ["taskId", "tagId"]
)
data class TaskTagEntity(
    val taskId: Int,
    val tagId: Int
)

data class TaskWithTags(
    @Embedded val task: TaskEntity,

    @Relation(
        parentColumn = "taskId",
        entityColumn = "tagId",
        associateBy = Junction(TaskTagEntity::class)
    )
    val tags: List<TagEntity>
)
