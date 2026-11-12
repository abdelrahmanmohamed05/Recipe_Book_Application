package com.example.myapplication.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

// Initialize DataStore instance for settings preferences
val Context.dataStore by preferencesDataStore(name = "settings")

class SettingsRepository(private val context: Context) {

    companion object {
        val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")
        val SORT_ORDER_KEY = stringPreferencesKey("sort_order")
    }

    // Expose dark mode preference as a reactive Flow
    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { preferences ->
            preferences[DARK_MODE_KEY] ?: false
        }

    // Expose optional sort order preference as a reactive Flow
    val sortOrderFlow: Flow<String?> = context.dataStore.data
        .map { preferences ->
            preferences[SORT_ORDER_KEY]
        }

    // Save dark mode setting persistently
    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[DARK_MODE_KEY] = enabled
        }
    }

    // Save sort order setting persistently
    suspend fun setSortOrder(sortOrder: String) {
        context.dataStore.edit { preferences ->
            preferences[SORT_ORDER_KEY] = sortOrder
        }
    }
}
