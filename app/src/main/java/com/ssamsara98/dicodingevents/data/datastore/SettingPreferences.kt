package com.ssamsara98.dicodingevents.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID
import javax.inject.Inject

class SettingPreferences
@Inject
constructor(private val dataStore: DataStore<Preferences>) {

    companion object {
        val THEME_KEY = booleanPreferencesKey("theme_setting")
        val DAILY_REMINDER_WORK_ID = stringPreferencesKey("daily_reminder_work_id")
    }

    fun getThemeSetting() = dataStore.data.map { preferences ->
        preferences[THEME_KEY] == true
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = dataStore.edit { preferences ->
        preferences[THEME_KEY] = isDarkModeActive
    }

    fun getDailyReminderWorkId() = dataStore.data.map { preferences ->
        preferences[DAILY_REMINDER_WORK_ID]
    }

    suspend fun saveDailyReminderWorkId(uuid: UUID?) = dataStore.edit { preferences ->
        if (uuid == null) {
            preferences[DAILY_REMINDER_WORK_ID] = ""
        } else {
            preferences[DAILY_REMINDER_WORK_ID] = uuid.toString()
        }
    }
}