package com.ssamsara98.dicodingevents.data.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity

@Database(entities = [FavoriteEventEntity::class], version = 1, exportSchema = false)
abstract class DicodingEventsDatabase : RoomDatabase() {
    abstract fun favoriteEventDao(): FavoriteEventDao
}