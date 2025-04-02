package com.ssamsara98.dicodingevents.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import kotlinx.coroutines.delay

class EventDetailViewModel(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _eventItem =
        MutableLiveData<Resource<EventItem?, Event<String>>>().apply { value = Resource.Loading }
    val eventItem: LiveData<Resource<EventItem?, Event<String>>> = _eventItem

    private val _isFavorite = MutableLiveData<Boolean>().apply { value = false }
    val isFavorite: LiveData<Boolean> = _isFavorite

    suspend fun changeEventItem(eventItem: EventItem) {
        delay(500)
        _isFavorite.value = repository.checkIsFavorite(eventItem.id)
        _eventItem.value = Resource.Success(eventItem)
    }

    suspend fun fetchEvent(id: String) {
        _eventItem.value = Resource.Loading
        try {
            val response = ApiConfig.getApiService().getEventByIdAsync(id)
            _eventItem.value = Resource.Success(response.event)
            _isFavorite.value = response.event?.let { repository.checkIsFavorite(it.id) }
        } catch (e: Exception) {
            _eventItem.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
            Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
        }
    }

    suspend fun toggleBookmark(favoriteEventEntity: FavoriteEventEntity) {
        if (_isFavorite.value == true) {
            repository.deleteFromFavorite(favoriteEventEntity.id)
            _isFavorite.value = false
        } else {
            repository.addToFavorite(favoriteEventEntity)
            _isFavorite.value = true
        }
    }
}