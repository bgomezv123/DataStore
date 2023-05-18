package com.example.datastore

import android.content.Context
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map



class PreferenceManager(context: Context) {
    val Context.dataStore by preferencesDataStore(name = "settings")
    private val dataStore = context.dataStore

    suspend fun saveName(name: String) {
        dataStore.edit { preferences ->
            preferences[PreferencesKeys.NAME] = name
        }
    }

    val nameFlow: Flow<String?> get() = dataStore.data.map {
            preferences ->
        preferences[PreferencesKeys.NAME]
    }



    private object PreferencesKeys {
        val NAME = stringPreferencesKey("name")
    }
}