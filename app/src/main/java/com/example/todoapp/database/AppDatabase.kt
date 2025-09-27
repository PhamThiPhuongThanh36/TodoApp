package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.todoapp.database.dao.ListDao
import com.example.todoapp.database.dao.ProjectDao
import com.example.todoapp.database.dao.TaskDao
import com.example.todoapp.database.entities.ListEntity
import com.example.todoapp.database.entities.ProjectEntity
import com.example.todoapp.database.entities.TagEntity
import com.example.todoapp.database.entities.TaskEntity
import com.example.todoapp.database.entities.TaskTagEntity

@Database(
    entities = [
        ProjectEntity::class,
        ListEntity::class,
        TaskEntity::class,
        TagEntity::class,
        TaskTagEntity::class,
    ],
    version = 6,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun listDao(): ListDao
    abstract fun taskDao(): TaskDao
}