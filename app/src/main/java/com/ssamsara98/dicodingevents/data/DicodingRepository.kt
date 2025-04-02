package com.ssamsara98.dicodingevents.data

import com.ssamsara98.dicodingevents.ApiService
import com.ssamsara98.dicodingevents.data.datastore.SettingDatastore
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.room.FavoriteEventDao

class DicodingRepository private constructor(
    private val apiService: ApiService,
    private val settingDatastore: SettingDatastore,
    private val favoriteEventDao: FavoriteEventDao
) {

    /* Event Detail */
    // fun fetchEvent(id: String) = liveData<Resource<EventItem?, Event<String>>> {
    //     emit(Resource.Loading)
    //     try {
    //         val eventResponse = apiService.getEventByIdAsync(id)
    //         emit(Resource.Success(eventResponse.event))
    //     } catch (e: Exception) {
    //         Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
    //         emit(Resource.Error(Event(e.message.toString())))
    //     }
    // }

    suspend fun addToFavorite(favoriteEventEntity: FavoriteEventEntity) =
        favoriteEventDao.insertFavorite(favoriteEventEntity)

    suspend fun deleteFromFavorite(id: Int) = favoriteEventDao.deleteFavoriteById(id)

    suspend fun checkIsFavorite(id: Int) = favoriteEventDao.checkIsFavorite(id)

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