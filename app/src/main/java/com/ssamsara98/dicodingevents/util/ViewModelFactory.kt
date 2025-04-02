package com.ssamsara98.dicodingevents.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssamsara98.dicodingevents.data.datastore.SettingPreferences
import com.ssamsara98.dicodingevents.ui.setting.SettingViewModel

class ViewModelFactory(private val pref: SettingPreferences) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(pref) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }
}