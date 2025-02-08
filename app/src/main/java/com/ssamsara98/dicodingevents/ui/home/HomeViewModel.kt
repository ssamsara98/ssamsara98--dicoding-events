package com.ssamsara98.dicodingevents.ui.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.response.EventItem
import com.ssamsara98.dicodingevents.response.EventsResponse
import com.ssamsara98.dicodingevents.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val _isLoadingUpcoming = MutableLiveData<Boolean>().apply { value = true }
    val isLoadingUpcoming: LiveData<Boolean> = _isLoadingUpcoming

    private val _upcomingEventList = MutableLiveData<List<EventItem>>()
    val upcomingEventList: LiveData<List<EventItem>> = _upcomingEventList

    private val _upcomingSnackBarTextFailed = MutableLiveData<Event<String>>()
    val upcomingSnackBarTextFailed: LiveData<Event<String>> = _upcomingSnackBarTextFailed

    private val _isLoadingFinished = MutableLiveData<Boolean>().apply { value = true }
    val isLoadingFinished: LiveData<Boolean> = _isLoadingFinished

    private val _finishedEventList = MutableLiveData<List<EventItem>>()
    val finishedEventList: LiveData<List<EventItem>> = _finishedEventList

    private val _finishedSnackBarTextFailed = MutableLiveData<Event<String>>()
    val finishedSnackBarTextFailed: LiveData<Event<String>> = _finishedSnackBarTextFailed

    init {
        load()
    }

    fun load() {
        fetchUpcomingEventList()
        fetchFinishedEventList()
    }

    private fun fetchUpcomingEventList() {
        _isLoadingUpcoming.value = true
        val client = ApiConfig.getApiService().getEventList(1, 5)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoadingUpcoming.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _upcomingEventList.value = body?.listEvents
                } else {
                    _upcomingSnackBarTextFailed.value = Event("onFailure: ${response.message()}")
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoadingUpcoming.value = false
                _upcomingSnackBarTextFailed.value = Event("onFailure: ${t.message.toString()}")
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }

    private fun fetchFinishedEventList() {
        _isLoadingFinished.value = true
        val client = ApiConfig.getApiService().getEventList(0, 5)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoadingFinished.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _finishedEventList.value = body?.listEvents
                } else {
                    _finishedSnackBarTextFailed.value = Event("onFailure: ${response.message()}")
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoadingFinished.value = false
                _finishedSnackBarTextFailed.value = Event("onFailure: ${t.message.toString()}")
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}