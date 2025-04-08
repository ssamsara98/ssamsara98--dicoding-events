package com.ssamsara98.dicodingevents.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SettingPreferences
@Inject
constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        val DARK_MODE = booleanPreferencesKey("is_dark_mode_enabled")
        val DAILY_REMINDER = booleanPreferencesKey("is_daily_reminder_enabled")
    }

    fun getDarkMode() = dataStore.data.map { preferences ->
        preferences[DARK_MODE] == true
    }

    suspend fun saveDarkMode(isEnabled: Boolean) = dataStore.edit { preferences ->
        preferences[DARK_MODE] = isEnabled
    }

    fun getDailyReminder() = dataStore.data.map { preferences ->
        preferences[DAILY_REMINDER] == true
    }

    suspend fun saveDailyReminder(isEnabled: Boolean) = dataStore.edit { preferences ->
        preferences[DAILY_REMINDER] = isEnabled
    }
}