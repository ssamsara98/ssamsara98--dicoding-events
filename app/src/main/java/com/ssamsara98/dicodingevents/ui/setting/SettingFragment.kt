package com.ssamsara98.dicodingevents.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.work.Constraints
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkInfo
import androidx.work.WorkManager
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import com.ssamsara98.dicodingevents.util.MyWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.UUID
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    companion object {
        // prevent switch runs twice when enabled
        private var isDarkModeActive: Boolean = false
        private var isDailyReminderActive: Boolean = false
        private var dailyReminderWorkId: UUID? = null
    }

    private val settingViewModel: SettingViewModel? by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        workManager = WorkManager.getInstance(requireActivity())

        settingViewModel?.apply {
            this.getThemeSettings().observe(viewLifecycleOwner) {
                isDarkModeActive = it
                binding.switchTheme.isChecked = isDarkModeActive
            }

            this.getDailyReminderWorkId().observe(viewLifecycleOwner) {
                if (it == null || it == "") {
                    dailyReminderWorkId = null
                    isDailyReminderActive = false
                    binding.switchDailyReminder.isChecked = false
                    return@observe
                }

                dailyReminderWorkId = UUID.fromString(it)
                isDailyReminderActive = true
                binding.switchDailyReminder.isChecked = true
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

    private fun makeSwitchToast(name: String, isChecked: Boolean) {
        Toast.makeText(
            activity,
            "$name is ${if (isChecked) "enabled" else "disabled"}",
            Toast.LENGTH_SHORT
        ).show()
    }

    override fun onCheckedChanged(
        buttonView: CompoundButton,
        isChecked: Boolean
    ) {
        with(binding) {
            when (buttonView.id) {
                switchTheme.id -> switchTheme(isChecked)
                switchDailyReminder.id -> switchDailyReminder(isChecked)
            }
        }
    }

    private fun switchTheme(
        isChecked: Boolean
    ) {
        if (isChecked == isDarkModeActive) return

        settingViewModel?.saveThemeSetting(isChecked)
        makeSwitchToast("Dark Mode", isChecked)
    }

    private fun switchDailyReminder(
        isChecked: Boolean
    ) {
        if (isChecked == isDailyReminderActive) return

        when (isChecked) {
            true -> startPeriodicTask()
            false -> cancelPeriodicTask()
        }
        makeSwitchToast("Daily Reminder", isChecked)
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        workManager.apply {
            enqueue(periodicWorkRequest)
            getWorkInfoByIdLiveData(periodicWorkRequest.id).observe(requireActivity()) { workInfo ->
                binding.switchDailyReminder.isEnabled = false
                if (workInfo?.state == WorkInfo.State.ENQUEUED || workInfo?.state == WorkInfo.State.CANCELLED) {
                    binding.switchDailyReminder.isEnabled = true
                }
            }
        }
        settingViewModel?.saveDailyReminderWorkId(periodicWorkRequest.id)
    }

    private fun cancelPeriodicTask() {
        if (dailyReminderWorkId == null) return

        Toast.makeText(
            activity,
            "cancelling periodic task (periodicWorkRequest.id := ${dailyReminderWorkId})",
            Toast.LENGTH_SHORT
        ).show()
        settingViewModel?.saveDailyReminderWorkId(null)
        workManager.cancelWorkById(dailyReminderWorkId!!)
    }
}