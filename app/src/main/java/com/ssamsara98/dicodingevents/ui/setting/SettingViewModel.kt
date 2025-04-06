package com.ssamsara98.dicodingevents.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ssamsara98.dicodingevents.data.DicodingRepository
import kotlinx.coroutines.launch
import java.util.UUID

class SettingViewModel(
    private val repository: DicodingRepository
) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> = repository.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    fun getDailyReminderWorkId(): LiveData<String> = repository.getDailyReminderWorkId().asLiveData()

    fun saveDailyReminderWorkId(uuid: UUID?) = viewModelScope.launch {
        repository.saveDailyReminderWorkId(uuid)
    }
}