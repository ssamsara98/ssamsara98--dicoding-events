package com.ssamsara98.dicodingevents.data.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity

@Dao
interface FavoriteEventDao {

    @Query("SELECT * FROM favorite_event ORDER BY begin_time DESC")
    fun getFavoriteEventList(): LiveData<List<FavoriteEventEntity>>

    @Query("SELECT * FROM favorite_event WHERE id = :id")
    fun getFavoriteEventById(id: String): FavoriteEventEntity

    @Query("SELECT EXISTS(SELECT id FROM favorite_event WHERE id = :id)")
    suspend fun checkIsFavorite(id: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFavorite(favoriteEventEntity: FavoriteEventEntity)

    @Query("DELETE FROM favorite_event WHERE id = :id")
    suspend fun deleteFavoriteById(id: Int)
}