package com.example.todoapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.todoapp.database.entities.ListEntity
import com.example.todoapp.repository.ListRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(private val repository: ListRepository) : ViewModel() {

    fun getListsByProjectId(projectId: Int): Flow<List<ListEntity>> {
        return repository.getListsByProjectId(projectId)
    }

    fun insertList(list: ListEntity) {
        viewModelScope.launch {
            repository.insertList(list)
        }
    }

    fun updateList(list: ListEntity) {
        viewModelScope.launch {
            repository.updateList(list)
        }
    }

    fun deleteList(listId: Int) {
        viewModelScope.launch {
            repository.deleteList(listId)
        }
    }

}