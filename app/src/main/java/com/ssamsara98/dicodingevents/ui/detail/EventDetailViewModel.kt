package com.ssamsara98.dicodingevents.ui.detail

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.entity.FavoriteEventEntity
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import javax.inject.Inject

@HiltViewModel
class EventDetailViewModel
@Inject
constructor(
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
        _isFavorite.value = repository.checkIsFavorite(id.toInt())
        try {
            val response = repository.getEventById(id)
            _eventItem.value = Resource.Success(response.event)
        } catch (e: Exception) {
            // Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
            _eventItem.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
        }
    }

    private fun toggleBookmarkToast(isTrue: Boolean, context: Context?) {
        Toast.makeText(
            context,
            "${if (isTrue) "Added to" else "Removed from"} Favorite",
            Toast.LENGTH_SHORT
        ).show()
    }

    suspend fun toggleBookmark(favoriteEventEntity: FavoriteEventEntity, context: Context?) {
        if (_isFavorite.value == true) {
            repository.deleteFromFavorite(favoriteEventEntity.id)
            _isFavorite.value = false
            toggleBookmarkToast(false, context)
        } else {
            repository.addToFavorite(favoriteEventEntity)
            _isFavorite.value = true
            toggleBookmarkToast(true, context)
        }
    }
}