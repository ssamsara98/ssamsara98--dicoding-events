package com.ssamsara98.dicodingevents.ui.finished

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.data.response.EventsResponse
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FinishedViewModel : ViewModel() {

    private val _eventListFinished = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventListFinished: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventListFinished

    init {
        fetchFinishedEventList()
    }

    fun load() {
        fetchFinishedEventList()
    }

    private fun fetchFinishedEventList() {
        _eventListFinished.value = Resource.Loading
        val client = ApiConfig.getApiService().getEventList(0)

        val callback = object : Callback<EventsResponse> {
            override fun onResponse(
                call: Call<EventsResponse>,
                response: Response<EventsResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventListFinished.value = Resource.Success(body?.listEvents)
                } else {
                    _eventListFinished.value =
                        Resource.Error(Event("onFailure: ${response.message()}"))
                    Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventsResponse>,
                t: Throwable
            ) {
                _eventListFinished.value =
                    Resource.Error(Event("onFailure: ${t.message.toString()}"))
                Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}