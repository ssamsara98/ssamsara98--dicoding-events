package com.ssamsara98.dicodingevents.ui.setting

import android.annotation.SuppressLint
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
import com.ssamsara98.dicodingevents.MyWorker
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import com.ssamsara98.dicodingevents.util.ViewModelFactory
import java.util.UUID
import java.util.concurrent.TimeUnit

class SettingFragment : Fragment(), CompoundButton.OnCheckedChangeListener, View.OnClickListener {

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

        workManager = WorkManager.getInstance(requireActivity())

        settingViewModel?.apply {
            this.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                Companion.isDarkModeActive = isDarkModeActive
                binding.switchTheme.isChecked = isDarkModeActive
            }
            this.getDailyReminderWorkId().observe(viewLifecycleOwner) {
                if (it == null || it == "") {
                    dailyReminderWorkId = null
                    binding.btnCancelTask.isEnabled = false
                    isDailyReminderActive = false
                    binding.switchDailyReminder.isChecked = false
                    return@observe
                }

                dailyReminderWorkId = UUID.fromString(it)
                binding.btnCancelTask.isEnabled = true
                isDailyReminderActive = true
                binding.switchDailyReminder.isChecked = true
                workManager.getWorkInfoByIdLiveData(dailyReminderWorkId!!)
                    .observe(requireActivity()) { workInfo ->
                        val status = workInfo?.state?.name
                        binding.textStatus.append("\n$status :=> $dailyReminderWorkId")
                        binding.btnCancelTask.isEnabled = false
                        if (workInfo?.state == WorkInfo.State.ENQUEUED) {
                            binding.btnCancelTask.isEnabled = true
                        }
                    }
            }
        }

        with(binding) {
            this.switchTheme.setOnCheckedChangeListener(this@SettingFragment)

            this.switchDailyReminder.setOnCheckedChangeListener(this@SettingFragment)

            this.btnPeriodicTask.setOnClickListener(this@SettingFragment)

            this.btnCancelTask.setOnClickListener(this@SettingFragment)
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

    override fun onClick(view: View) {
        with(binding) {
            when (view.id) {
                btnPeriodicTask.id -> {
                    startPeriodicTask()
                    Toast.makeText(
                        activity,
                        "btnPeriodicTask (${view.id} == ${binding.btnPeriodicTask.id}) is pressed",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                btnCancelTask.id -> {
                    cancelPeriodicTask()
                    Toast.makeText(
                        activity,
                        "btnCancelTask (${view.id} == ${binding.btnCancelTask.id}) is pressed",
                        Toast.LENGTH_SHORT
                    ).show()
                }
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
            "Dark Mode (${buttonView?.id} == ${binding.switchTheme.id}) is ${if (isChecked) "enabled" else "disabled"}",
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
            "Daily Reminder (${buttonView?.id} == ${binding.switchDailyReminder.id}) is ${if (isChecked) "enabled" else "disabled"}",
            Toast.LENGTH_SHORT
        ).show()
    }

    @SuppressLint("SetTextI18n")
    private fun startPeriodicTask() {
        binding.textStatus.text = "Status :"
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()
        periodicWorkRequest =
            PeriodicWorkRequest.Builder(MyWorker::class.java, 15, TimeUnit.MINUTES)
                .setConstraints(constraints)
                .build()
        workManager.enqueue(periodicWorkRequest)
        settingViewModel?.saveDailyReminderWorkId(periodicWorkRequest.id)
    }

    private fun cancelPeriodicTask() {
        if (dailyReminderWorkId == null) return
        Toast.makeText(activity, "dailyReminderWorkId := $dailyReminderWorkId", Toast.LENGTH_SHORT)
            .show()
        settingViewModel?.saveDailyReminderWorkId(null)
        workManager.cancelWorkById(dailyReminderWorkId!!)
    }
}