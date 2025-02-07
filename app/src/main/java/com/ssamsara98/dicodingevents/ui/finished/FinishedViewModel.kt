package com.ssamsara98.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.response.EventItem
import com.ssamsara98.dicodingevents.response.EventsResponse
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventListFinished = MutableLiveData<List<EventItem>>()
    val eventListFinished: LiveData<List<EventItem>> = _eventListFinished

    init {
        fetchFinishedEventList()
    }

    private fun fetchFinishedEventList() {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventList(active = 0)
        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventListFinished.value = body?.listEvents
                } else {
                    Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<EventsResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}