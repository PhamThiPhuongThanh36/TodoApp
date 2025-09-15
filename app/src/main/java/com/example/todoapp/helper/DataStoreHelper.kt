package com.example.todoapp.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")
val CURRENT_PROJECT_ID = intPreferencesKey("current_project_id")

object DataStoreHelper {
    suspend fun saveCurrentProjectId(context: Context, projectId: Int) {
        context.dataStore.edit { preferences ->
            preferences[CURRENT_PROJECT_ID] = projectId
        }
    }

    suspend fun getCurrentProjectId(context: Context): Int {
        val preferences = context.dataStore.data.first()
        return preferences[CURRENT_PROJECT_ID] ?: -1
    }
}