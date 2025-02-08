package com.ssamsara98.dicodingevents.ui.detail

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.ApiConfig
import com.ssamsara98.dicodingevents.response.EventItem
import com.ssamsara98.dicodingevents.response.EventResponse
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel
import com.ssamsara98.dicodingevents.util.Event
import kotlinx.coroutines.delay
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class EventDetailViewModel : ViewModel() {

    private val _isLoading = MutableLiveData<Boolean>().apply { value = true }
    val isLoading: LiveData<Boolean> = _isLoading

    private val _eventItem = MutableLiveData<EventItem?>()
    val eventItem: LiveData<EventItem?> = _eventItem

    private val _snackBarTextFailed = MutableLiveData<Event<String>>()
    val snackBarTextFailed: LiveData<Event<String>> = _snackBarTextFailed

    suspend fun changeEventItem(eventItem: EventItem) {
        delay(500)
        _isLoading.value = false
        _eventItem.value = eventItem
    }

    fun fetchEvent(id: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getEventById(id)

        val callback = object : Callback<EventResponse> {
            override fun onResponse(
                call: Call<EventResponse>,
                response: Response<EventResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    _eventItem.value = body?.event
                } else {
                    _snackBarTextFailed.value = Event("onFailure: ${response.message()}")
                    Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(
                call: Call<EventResponse>,
                t: Throwable
            ) {
                _isLoading.value = false
                _snackBarTextFailed.value = Event("onFailure: ${t.message.toString()}")
                Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${t.message.toString()}")
            }
        }
        client.enqueue(callback)
    }
}