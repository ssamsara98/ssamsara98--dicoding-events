package com.ssamsara98.dicodingevents.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.util.ApiConfig
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.data.response.EventsResponse
import com.ssamsara98.dicodingevents.ui.home.HomeViewModel
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _eventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventList

    suspend fun fetchSearch(q: String) {
        _eventList.value = Resource.Loading
        try {
            val response = ApiConfig.getApiService().getEventListAsync(-1, q = q)
            _eventList.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            _eventList.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
            Log.e(HomeViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
        }
    }
}