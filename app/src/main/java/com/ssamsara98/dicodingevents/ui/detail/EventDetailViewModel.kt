package com.ssamsara98.dicodingevents.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.response.EventItem
import kotlinx.coroutines.delay

class EventDetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventItem = MutableLiveData<EventItem>()
    val eventItem: LiveData<EventItem> = _eventItem

    suspend fun changeEventItem(eventItem: EventItem) {
        delay(500)
        _isLoading.value = false
        _eventItem.value = eventItem
    }
}