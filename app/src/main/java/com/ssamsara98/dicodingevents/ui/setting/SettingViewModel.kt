package com.ssamsara98.dicodingevents.ui.setting

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ssamsara98.dicodingevents.data.DicodingRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.util.UUID
import javax.inject.Inject

@HiltViewModel
class SettingViewModel
@Inject
constructor(
    private val repository: DicodingRepository
) : ViewModel() {
    fun getThemeSettings() = repository.getThemeSetting().asLiveData()

    fun saveThemeSetting(isDarkModeActive: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkModeActive)
    }

    fun getDailyReminderWorkId() =
        repository.getDailyReminderWorkId().asLiveData()

    fun saveDailyReminderWorkId(uuid: UUID?) = viewModelScope.launch {
        repository.saveDailyReminderWorkId(uuid)
    }
}