package com.ssamsara98.dicodingevents.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity

@Database(entities = [FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class DicodingEventsDatabase : RoomDatabase() {

    abstract fun favoriteEventDao(): FavoriteEventDao

    companion object {
        @Volatile
        private var instance: DicodingEventsDatabase? = null

        fun getInstance(context: Context): DicodingEventsDatabase = instance ?: synchronized(this) {
            instance ?: Room.databaseBuilder(
                context.applicationContext,
                DicodingEventsDatabase::class.java,
                "DicodingEvents.db"
            ).build().also { instance = it }
        }
    }
}