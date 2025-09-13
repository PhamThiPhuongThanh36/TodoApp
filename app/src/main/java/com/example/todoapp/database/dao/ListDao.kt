package com.example.todoapp.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.todoapp.database.entities.ListEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ListDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertList(list: ListEntity)

    @Query("SELECT * FROM lists WHERE projectId = :projectId")
    fun getListsByProjectId(projectId: Int): Flow<List<ListEntity>>

    @Query("DELETE FROM lists WHERE listId = :listId")
    suspend fun deleteList(listId: Int)

    @Update
    suspend fun updateList(list: ListEntity)
}