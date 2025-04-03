package com.ssamsara98.dicodingevents.ui.upcoming

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import kotlinx.coroutines.launch

class UpcomingViewModel(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _eventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventList

    init {
        viewModelScope.launch {
            fetchUpcomingEventList()
        }
    }

    suspend fun load() {
        fetchUpcomingEventList()
    }

    private suspend fun fetchUpcomingEventList() {
        _eventList.value = Resource.Loading
        try {
            val response = repository.getEventListAsync(1)
            _eventList.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            _eventList.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
            Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
        }
    }
}