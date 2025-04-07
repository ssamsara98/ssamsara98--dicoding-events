package com.ssamsara98.dicodingevents.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.data.response.EventItem
import com.ssamsara98.dicodingevents.util.Event
import com.ssamsara98.dicodingevents.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FinishedViewModel
@Inject
constructor(
    private val repository: DicodingRepository
) : ViewModel() {

    private val _eventListFinished = MutableLiveData<Resource<List<EventItem>?, Event<String>>>()
    val eventListFinished: LiveData<Resource<List<EventItem>?, Event<String>>> = _eventListFinished

    init {
        viewModelScope.launch {
            fetchFinishedEventList()
        }
    }

    suspend fun load() {
        fetchFinishedEventList()
    }

    private suspend fun fetchFinishedEventList() {
        _eventListFinished.value = Resource.Loading
        try {
            val response = repository.getEventList(0)
            _eventListFinished.value = Resource.Success(response.listEvents)
        } catch (e: Exception) {
            // Log.e(UpcomingViewModel::class.simpleName, "onFailure: ${e.message.toString()}")
            _eventListFinished.value = Resource.Error(Event("onFailure: ${e.message.toString()}"))
        }
    }
}