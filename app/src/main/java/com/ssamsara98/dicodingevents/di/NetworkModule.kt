package com.ssamsara98.dicodingevents.di

import com.ssamsara98.dicodingevents.util.ApiConfig
import com.ssamsara98.dicodingevents.util.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideUserApiService(): ApiService {
        return ApiConfig.getApiService()
    }
}
