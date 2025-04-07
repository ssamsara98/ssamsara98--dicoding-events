package com.ssamsara98.dicodingevents.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _eventList = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventList: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventList

    suspend fun fetchSearch(q: String) {
        _eventList.value = Resource.Loading
        try {
            val response = repository.getEventList(-1, q = q)
            _eventList.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            // Log.e(HomeViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
            _eventList.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
        }
    }
}