package com.ssamsara98.dicodingevents.ui.favorite

import androidx.lifecycle.ViewModel
import com.ssamsara98.dicodingevents.data.DicodingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FavoriteViewModel
@Inject
constructor(
    private val repository: DicodingRepository
) : ViewModel() {
    fun getFavoriteEventList() = repository.getFavoriteEventList()
}