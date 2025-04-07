package com.ssamsara98.dicodingevents.di

import android.content.Context
import androidx.room.Room
import com.ssamsara98.dicodingevents.data.room.DicodingEventsDatabase
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDicodingEventsDatabase(@ApplicationContext context: Context): DicodingEventsDatabase =
        Room.databaseBuilder(
            context,
            DicodingEventsDatabase::class.java,
            "DicodingEvents.db"
        ).build()

    @Provides
    fun provideFavoriteEventDao(database: DicodingEventsDatabase): FavoriteEventDao =
        database.favoriteEventDao()

}
