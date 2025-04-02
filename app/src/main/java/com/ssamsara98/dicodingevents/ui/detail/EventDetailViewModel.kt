package com.ssamsara98.dicodingevents.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.data.response.EventResponse
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {

    private val _eventItem = MutableLiveData<Resource<EventItem?, Event<String>>>().apply { value = Resource.Loading }
    val eventItem: LiveData<Resource<EventItem?, Event<String>>> = _eventItem

    suspend fun changeEventItem(eventItem: EventItem) {
        delay(500)
        _eventItem.value = Resource.Success(eventItem)
    }

    fun fetchEvent(id: String) {
        _eventItem.value = Resource.Loading
        val client = ApiConfig.getApiService().getEventById(id)

        val callback = object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventItem.value = Resource.Success(body?.event)
                } else {
                    _eventItem.value = Resource.Error(Event("onFailure: ${response.message()}"))
                    Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventResponse>,
                t: Throwable
            ) {
                _eventItem.value = Resource.Error(Event("onFailure: ${t.message.toString()}"))
                Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}