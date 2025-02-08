package com.ssamsara98.dicodingevents.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.response.EventItem
import com.ssamsara98.dicodingevents.util.Event
import kotlinx.coroutines.delay

class EventDetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventItem = MutableLiveData<EventItem>()
    val eventItem: LiveData<EventItem> = _eventItem

    private val _snackBarTextFailed = MutableLiveData<Event<String>>()
    val snackBarTextFailed: LiveData<Event<String>> = _snackBarTextFailed

    suspend fun changeEventItem(eventItem: EventItem) {
        delay(500)
        _isLoading.value = false
        _eventItem.value = eventItem
    }

    suspend fun fetchEvent() {
        _isLoading.value = true
        delay(2000)
        _isLoading.value = false
    }
}