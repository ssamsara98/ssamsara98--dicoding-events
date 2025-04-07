package com.ssamsara98.dicodingevents.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.ssamsara98.dicodingevents.data.datastore.SettingPreferences
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao
import com.ssamsara98.dicodingevents.util.ApiService
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import javax.inject.Inject

class DicodingRepository
@Inject
constructor(
    private val apiService: ApiService,
    private val settingPreferences: SettingPreferences,
    private val favoriteEventDao: FavoriteEventDao
) {

    /* Event Detail */
    suspend fun getEventList(active: Int? = null, limit: Int? = null, q: String? = null) =
        apiService.getEventList(active = active, limit = limit, q = q)

    suspend fun getEventById(id: String) = apiService.getEventById(id)

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
                // Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
                emit(Resource.Error(Event(e.message.toString())))
            }
        }

    /* Setting */
    fun getThemeSetting() = settingPreferences.getThemeSetting()

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        settingPreferences.saveThemeSetting(isDarkModeActive)
}