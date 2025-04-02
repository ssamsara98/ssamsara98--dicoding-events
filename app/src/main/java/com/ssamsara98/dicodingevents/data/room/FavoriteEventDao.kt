package com.ssamsara98.dicodingevents.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event ORDER BY begin_time DESC")
    fun getFavoriteEventList(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteEventById(id: String): LiveData<FavoriteEventEntity>
}