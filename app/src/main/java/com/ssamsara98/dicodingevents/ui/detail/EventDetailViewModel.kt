package com.ssamsara98.dicodingevents.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.response.EventItem

class EventDetailViewModel: ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventItem = MutableLiveData<EventItem>()
    val eventItem: LiveData<EventItem> = _eventItem

    fun changeEventItem(eventItem: EventItem) {
        _isLoading.value = false
        _eventItem.value = eventItem
    }
}