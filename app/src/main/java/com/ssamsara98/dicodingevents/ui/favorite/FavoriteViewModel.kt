package com.ssamsara98.dicodingevents.ui.favorite

import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.data.DicodingRepository

class FavoriteViewModel(
    private val repository: DicodingRepository
) : ViewModel() {
    fun getFavoriteEventList() = repository.getFavoriteEventList()
}