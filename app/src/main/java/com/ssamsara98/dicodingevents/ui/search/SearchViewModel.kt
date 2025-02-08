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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchViewModel : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>().apply { value = false }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventList = MutableLiveData<List<EventItem>>()
    val eventList: LiveData<List<EventItem>> = _eventList

    private val _snackBarTextFailed = MutableLiveData<Event<String>>()
    val snackBarTextFailed: LiveData<Event<String>> = _snackBarTextFailed

    fun fetchSearch(q: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventList(-1, query = q)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventList.value = body?.listEvents
                } else {
                    _snackBarTextFailed.value = Event("onFailure: ${response.message()}")
                    Log.e(HomeViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                _snackBarTextFailed.value = Event("onFailure: ${t.message.toString()}")
                Log.e(HomeViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}