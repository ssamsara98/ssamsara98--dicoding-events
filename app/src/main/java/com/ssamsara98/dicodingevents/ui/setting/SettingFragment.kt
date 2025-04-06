package com.ssamsara98.dicodingevents.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import com.ssamsara98.dicodingevents.util.ViewModelFactory

class SettingFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.


    companion object {
        // prevent switch runs twice when enabled
        private var isDarkModeActive: Boolean = false
        private var isDailyReminderActive: Boolean = false
    }

    private val settingViewModel: SettingViewModel? by viewModels {
        activity?.let { ViewModelFactory.getInstance(it) }!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingViewModel?.apply {
            this.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                Companion.isDarkModeActive = isDarkModeActive
                binding.switchTheme.isChecked = isDarkModeActive
            }
        }

        with(binding) {
            this.switchTheme.setOnCheckedChangeListener(this@SettingFragment)

            this.switchDailyReminder.setOnCheckedChangeListener(this@SettingFragment)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton?,
        isChecked: Boolean
    ) {
        with(binding) {
            when (buttonView?.id) {
                switchTheme.id -> switchTheme(buttonView, isChecked)
                switchDailyReminder.id -> switchDailyReminder(buttonView, isChecked)
            }
        }
    }

    private fun switchTheme(
        buttonView: CompoundButton?,
        isChecked: Boolean
    ) {
        if (isChecked == isDarkModeActive) return

        settingViewModel?.saveThemeSetting(isChecked)
        Toast.makeText(
            activity,
            "Dark Mode is ${if (isChecked) "enabled" else "disabled"}",
            Toast.LENGTH_SHORT
        ).show()
    }

    private fun switchDailyReminder(
        buttonView: CompoundButton?,
        isChecked: Boolean
    ) {
        if (isChecked == isDailyReminderActive) return

        isDailyReminderActive = isChecked
        Toast.makeText(
            activity,
            "Daily Reminder is ${if (isChecked) "enabled" else "disabled"}",
            Toast.LENGTH_SHORT
        ).show()
    }
}