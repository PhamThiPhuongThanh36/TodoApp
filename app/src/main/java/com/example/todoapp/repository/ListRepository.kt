package com.example.todoapp.repository

import com.example.todoapp.database.dao.ListDao
import com.example.todoapp.database.entities.ListEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ListRepository @Inject constructor(
    private val listDao: ListDao
) {
    suspend fun insertList(list: ListEntity) {
        listDao.insertList(list)
    }

    fun getListsByProjectId(projectId: Int): Flow<List<ListEntity>> {
        return listDao.getListsByProjectId(projectId)
    }

    suspend fun deleteList(listId: Int) {
        listDao.deleteList(listId)
    }

    suspend fun updateList(list: ListEntity) {
        listDao.updateList(list)
    }

}