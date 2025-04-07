package com.ssamsara98.dicodingevents.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.ssamsara98.dicodingevents.data.datastore.SettingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

@Module
@InstallIn(SingletonComponent::class)
object DataStoreModule {

    // private const val DATASTORE_NAME = "settings"

    // @Provides
    // @Singleton
    // fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> {
    //     return PreferenceDataStoreFactory.create(produceFile = {
    //         context.dataStoreFile(DATASTORE_NAME)
    //     })
    // }

    @Provides
    @Singleton
    fun provideSettingPreferences(@ApplicationContext context: Context): SettingPreferences {
        return SettingPreferences(context.dataStore)
    }
}
