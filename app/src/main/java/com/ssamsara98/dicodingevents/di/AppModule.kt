package com.ssamsara98.dicodingevents.di

import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.datastore.SettingPreferences
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao
import com.ssamsara98.dicodingevents.util.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideDicodingRepository(
        apiService: ApiService,
        settingPreferences: SettingPreferences,
        favoriteEventDao: FavoriteEventDao
    ): DicodingRepository {
        return DicodingRepository(apiService, settingPreferences, favoriteEventDao)
    }
}