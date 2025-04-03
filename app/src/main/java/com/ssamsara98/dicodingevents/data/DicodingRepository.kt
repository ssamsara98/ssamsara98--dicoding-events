package com.ssamsara98.dicodingevents.data

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.ssamsara98.dicodingevents.util.ApiService
import com.ssamsara98.dicodingevents.data.datastore.SettingDatastore
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource

class DicodingRepository private constructor(
    private val apiService: ApiService,
    private val settingDatastore: SettingDatastore,
    private val favoriteEventDao: FavoriteEventDao
) {

    /* Event Detail */
    suspend fun getEventByIdAsync(id: String) = apiService.getEventByIdAsync(id)

    /* Favorite Event */
    suspend fun addToFavorite(favoriteEventEntity: FavoriteEventEntity) =
        favoriteEventDao.insertFavorite(favoriteEventEntity)

    suspend fun deleteFromFavorite(id: Int) = favoriteEventDao.deleteFavoriteById(id)

    suspend fun checkIsFavorite(id: Int) = favoriteEventDao.checkIsFavorite(id)

    fun getFavoriteEventList() =
        liveData {
            emit(Resource.Loading)
            try {
                val localData: LiveData<Resource<List<FavoriteEventEntity>, Event<String>>> =
                    favoriteEventDao.getFavoriteEventList().map { Resource.Success(it) }
                emitSource(localData)
            } catch (e: Exception) {
                emit(Resource.Error(Event(e.message.toString())))
                Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
            }
        }

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