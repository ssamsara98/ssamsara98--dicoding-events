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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.PeriodicWorkRequest
import androidx.work.WorkManager
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import com.ssamsara98.dicodingevents.util.DailyReminderWorker
import dagger.hilt.android.AndroidEntryPoint
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SettingFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    private val settingViewModel: SettingViewModel? by viewModels()

    private lateinit var workManager: WorkManager
    private lateinit var periodicWorkRequest: PeriodicWorkRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        workManager = WorkManager.getInstance(requireActivity())

        settingViewModel?.apply {
            getDarkMode().observe(viewLifecycleOwner) { isEnabled ->
                binding.apply {
                    switchDarkMode.setOnCheckedChangeListener(null)
                    switchDarkMode.isChecked = isEnabled
                    switchDarkMode.setOnCheckedChangeListener(this@SettingFragment)
                }
            }

            getDailyReminder().observe(viewLifecycleOwner) { isEnabled ->
                binding.apply {
                    switchDailyReminder.setOnCheckedChangeListener(null)
                    switchDailyReminder.isChecked = isEnabled
                    switchDailyReminder.setOnCheckedChangeListener(this@SettingFragment)
                }
            }
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
        when (buttonView.id) {
            binding.switchDarkMode.id -> switchTheme(isChecked)
            binding.switchDailyReminder.id -> switchDailyReminder(isChecked)
        }
    }

    private fun switchTheme(
        isChecked: Boolean
    ) {
        settingViewModel?.saveDarkMode(isChecked)
        makeSwitchToast("Dark Mode", isChecked)
    }

    private fun switchDailyReminder(
        isChecked: Boolean
    ) {
        when (isChecked) {
            true -> startPeriodicTask()
            false -> cancelPeriodicTask()
        }
        settingViewModel?.saveDailyReminder(isChecked)
        makeSwitchToast("Daily Reminder", isChecked)
    }

    private fun startPeriodicTask() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(DailyReminderWorker::class.java, 1, TimeUnit.DAYS)
                .setConstraints(constraints)
                .build()
        workManager.enqueueUniquePeriodicWork(
            DailyReminderWorker.WORK_NAME,
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )
    }

    private fun cancelPeriodicTask() {
        Toast.makeText(
            activity,
            "cancelling periodic task \"${DailyReminderWorker.WORK_NAME}\"",
            Toast.LENGTH_SHORT
        ).show()
        workManager.cancelUniqueWork(DailyReminderWorker.WORK_NAME)
    }
}