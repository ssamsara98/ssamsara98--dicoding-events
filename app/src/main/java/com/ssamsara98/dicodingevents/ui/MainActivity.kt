package com.ssamsara98.dicodingevents.ui

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.ssamsara98.dicodingevents.R
import com.ssamsara98.dicodingevents.databinding.ActivityMainBinding
import com.ssamsara98.dicodingevents.ui.setting.SettingViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    // private val settingViewModel by viewModels<SettingViewModel> { ViewModelFactory.getInstance(this) }
    private val settingViewModel by viewModels<SettingViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_upcoming,
                R.id.navigation_finished,
                R.id.navigation_favorite,
                R.id.navigation_setting,
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        // dark mode
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            AppCompatDelegate.setDefaultNightMode(
                if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                else AppCompatDelegate.MODE_NIGHT_NO
            )
        }
    }
}