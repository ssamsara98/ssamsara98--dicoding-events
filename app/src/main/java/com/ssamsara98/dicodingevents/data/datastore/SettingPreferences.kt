package com.ssamsara98.dicodingevents.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingPreferences
@Inject
constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")

    fun getThemeSetting(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[themeKey] == true
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = dataStore.edit { preferences ->
        preferences[themeKey] = isDarkModeActive
    }
}