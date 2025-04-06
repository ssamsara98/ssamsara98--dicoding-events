package com.ssamsara98.dicodingevents.data.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.UUID

class SettingDatastore private constructor(private val dataStore: DataStore<Preferences>) {

    private val themeKey = booleanPreferencesKey("theme_setting")
    private val dailyReminderWorkIdKey = stringPreferencesKey("daily_reminder_work_id")

    fun getThemeSetting(): Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[themeKey] == true
    }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) = dataStore.edit { preferences ->
        preferences[themeKey] = isDarkModeActive
    }

    fun getDailyReminderWorkId(): Flow<String> = dataStore.data.map { preferences ->
        preferences[dailyReminderWorkIdKey].toString()
    }

    suspend fun saveDailyReminderWorkId(uuid: UUID?) = dataStore.edit { preferences ->
        if (uuid == null) {
            preferences[dailyReminderWorkIdKey] = ""
        } else {
            preferences[dailyReminderWorkIdKey] = uuid.toString()
        }
    }

    companion object {
        @Volatile
        private var instance: SettingDatastore? = null

        fun getInstance(dataStore: DataStore<Preferences>): SettingDatastore {
            return instance ?: synchronized(this) {
                instance ?: SettingDatastore(dataStore)
            }.also { instance = it }
        }
    }
}
