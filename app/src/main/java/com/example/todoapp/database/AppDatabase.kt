package com.example.todoapp.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
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
        TaskTagEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun projectDao(): ProjectDao
    abstract fun listDao(): ListDao
    abstract fun taskDao(): TaskDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "todo_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }

}