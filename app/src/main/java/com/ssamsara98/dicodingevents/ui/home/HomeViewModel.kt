package com.ssamsara98.dicodingevents.ui.home

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

class HomeViewModel(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _upcomingEventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val upcomingEventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _upcomingEventList

    private val _finishedEventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val finishedEventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _finishedEventList

    init {
        viewModelScope.launch {
            load()
        }
    }

    suspend fun load() {
        fetchUpcomingEventList()
        fetchFinishedEventList()
    }

    private suspend fun fetchUpcomingEventList() {
        _upcomingEventList.value = Resource.Loading

        try {
            val response = repository.getEventListAsync(1, 5)
            _upcomingEventList.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            // Log.e(HomeViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
            _upcomingEventList.value =
                Resource.Error(Event("onFailure: ${e.message.toString()}"))
        }
    }


    private suspend fun fetchFinishedEventList() {
        _finishedEventList.value = Resource.Loading
        try {
            val response = repository.getEventListAsync(0, 5)
            _finishedEventList.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            // Log.e(HomeViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
            _finishedEventList.value =
                Resource.Error(Event("onFailure: ${e.message.toString()}"))
        }
    }
}