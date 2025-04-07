package com.ssamsara98.dicodingevents.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ssamsara98.dicodingevents.util.ApiConfig
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.datastore.SettingPreferences
import com.ssamsara98.dicodingevents.data.room.DicodingEventsDatabase

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

object Injection {
    fun provideRepository(context: Context): DicodingRepository {
        val apiService = ApiConfig.getApiService()
        val database = DicodingEventsDatabase.getInstance(context)
        val settingPreferences = SettingPreferences.getInstance(context.dataStore)
        val favoriteEventDao = database.favoriteEventDao()

        return DicodingRepository.getInstance(
            apiService,
            settingPreferences,
            favoriteEventDao,
        )
    }
}