package com.ssamsara98.dicodingevents.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.util.ApiConfig
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.data.response.EventsResponse
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _upcomingEventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val upcomingEventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _upcomingEventList

    private val _finishedEventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val finishedEventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _finishedEventList

    init {
        load()
    }

    fun load() {
        fetchUpcomingEventList()
        fetchFinishedEventList()
    }

    private fun fetchUpcomingEventList() {
        _upcomingEventList.value = Resource.Loading
        val client = ApiConfig.getApiService().getEventList(1, 5)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _upcomingEventList.value = Resource.Success(body?.listEvents)
                } else {
                    _upcomingEventList.value =
                        Resource.Error(Event("onFailure: ${response.message()}"))
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventsResponse>,
                t: Throwable
            ) {
                _upcomingEventList.value =
                    Resource.Error(Event("onFailure: ${t.message.toString()}"))
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }

    private fun fetchFinishedEventList() {
        _finishedEventList.value = Resource.Loading
        val client = ApiConfig.getApiService().getEventList(0, 5)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _finishedEventList.value = Resource.Success(body?.listEvents)
                } else {
                    _finishedEventList.value =
                        Resource.Error(Event("onFailure: ${response.message()}"))
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventsResponse>,
                t: Throwable
            ) {
                _finishedEventList.value =
                    Resource.Error(Event("onFailure: ${t.message.toString()}"))
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}