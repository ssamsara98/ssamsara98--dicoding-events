package com.ssamsara98.dicodingevents.ui.search

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.response.EventItem
import com.ssamsara98.dicodingevents.response.EventsResponse
import com.ssamsara98.dicodingevents.ui.home.HomeViewModel
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {

    private val _eventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventList

    fun fetchSearch(q: String) {
        _eventList.value = Resource.Loading
        val client = ApiConfig.getApiService().getEventList(-1, query = q)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventList.value = Resource.Success(body?.listEvents)
                } else {
                    _eventList.value = Resource.Error(Event("onFailure: ${response.message()}"))
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventsResponse>,
                t: Throwable
            ) {
                _eventList.value = Resource.Error(Event("onFailure: ${t.message.toString()}"))
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}