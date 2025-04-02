package com.ssamsara98.dicodingevents.data

import com.ssamsara98.dicodingevents.ApiService
import com.ssamsara98.dicodingevents.data.datastore.SettingDatastore
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao

class DicodingRepository private constructor(
    private val apiService: ApiService,
    private val settingDatastore: SettingDatastore,
    private val favoriteEventDao: FavoriteEventDao
) {

    /* Setting */
    fun getThemeSetting() = settingDatastore.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        settingDatastore.saveThemeSetting(isDarkModeActive)

    companion object {
        @Volatile
        private var instance: DicodingRepository? = null

        fun getInstance(
            apiService: ApiService,
            settingDatastore: SettingDatastore,
            favoriteEventDao: FavoriteEventDao
        ): DicodingRepository = instance ?: synchronized(this) {
            instance ?: DicodingRepository(
                apiService = apiService,
                settingDatastore = settingDatastore,
                favoriteEventDao = favoriteEventDao
            )
        }.also { instance = it }
    }
}