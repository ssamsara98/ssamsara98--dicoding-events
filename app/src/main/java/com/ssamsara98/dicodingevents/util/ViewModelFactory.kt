package com.ssamsara98.dicodingevents.util

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ssamsara98.dicodingevents.data.DicodingRepository
import com.ssamsara98.dicodingevents.di.Injection
import com.ssamsara98.dicodingevents.ui.detail.EventDetailViewModel
import com.ssamsara98.dicodingevents.ui.favorite.FavoriteViewModel
import com.ssamsara98.dicodingevents.ui.finished.FinishedViewModel
import com.ssamsara98.dicodingevents.ui.home.HomeViewModel
import com.ssamsara98.dicodingevents.ui.search.SearchViewModel
import com.ssamsara98.dicodingevents.ui.setting.SettingViewModel
import com.ssamsara98.dicodingevents.ui.upcoming.UpcomingViewModel

class ViewModelFactory(
    private val repository: DicodingRepository
) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            return SettingViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(EventDetailViewModel::class.java)) {
            return EventDetailViewModel(repository) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)) {
            return FavoriteViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            return HomeViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(UpcomingViewModel::class.java)) {
            return UpcomingViewModel(repository) as T
        }
        else if (modelClass.isAssignableFrom(FinishedViewModel::class.java)) {
            return FinishedViewModel(repository) as T
        }
        // else if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
        //     return SearchViewModel(repository) as T
        // }
        throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null

        fun getInstance(context: Context): ViewModelFactory = instance ?: synchronized(this) {
            instance ?: ViewModelFactory(Injection.provideRepository(context))
        }.also { instance = it }
    }
}