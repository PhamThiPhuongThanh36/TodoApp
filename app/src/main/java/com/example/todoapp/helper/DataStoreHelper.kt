package com.example.todoapp.helper

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import kotlinx.coroutines.flow.first

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "dataStore")
val CURRENT_PROJECT_ID = intPreferencesKey("current_project_id")
val IS_RUNNING = booleanPreferencesKey("is_running")
val IS_RINGING = booleanPreferencesKey("is_ringing")
val TRIGGER_TIME = longPreferencesKey("trigger_time")
val TOTAL_TIME = longPreferencesKey("total_time")

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

    suspend fun saveIsRunning(context: Context, isRunning: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_RUNNING] = isRunning
        }
    }
    
    suspend fun getIsRunning(context: Context): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_RUNNING] ?: false
    }

    suspend fun saveIsRinging(context: Context, isRinging: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[IS_RINGING] = isRinging
        }
    }

    suspend fun getIsRinging(context: Context): Boolean {
        val preferences = context.dataStore.data.first()
        return preferences[IS_RINGING] ?: false
    }

    suspend fun saveTriggerTime(context: Context, triggerTime: Long) {
        context.dataStore.edit { preferences ->
            preferences[TRIGGER_TIME] = triggerTime
        }
    }

    suspend fun getTriggerTime(context: Context): Long {
        val preferences = context.dataStore.data.first()
        return preferences[TRIGGER_TIME] ?: 0L
    }
}