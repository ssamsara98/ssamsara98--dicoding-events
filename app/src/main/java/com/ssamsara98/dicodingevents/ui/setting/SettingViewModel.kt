package com.ssamsara98.dicodingevents.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.viewModelScope
import com.ssamsara98.dicodingevents.data.DicodingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject
constructor(
    private val repository: DicodingRepository
) : ViewModel() {

    fun getDarkMode() = repository.getDarkMode().asLiveData().distinctUntilChanged()

    fun saveDarkMode(isEnabled: Boolean) =
        viewModelScope.launch { repository.saveDarkMode(isEnabled) }

    fun getDailyReminder() = repository.getDailyReminder().asLiveData().distinctUntilChanged()

    fun saveDailyReminder(isEnabled: Boolean) =
        viewModelScope.launch { repository.saveDailyReminder(isEnabled) }

}