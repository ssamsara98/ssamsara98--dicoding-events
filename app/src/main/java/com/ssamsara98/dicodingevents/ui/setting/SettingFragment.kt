package com.ssamsara98.dicodingevents.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import com.ssamsara98.dicodingevents.util.ViewModelFactory

class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val factory = activity?.let { ViewModelFactory.getInstance(it) }
        val settingViewModel =
            factory?.let { ViewModelProvider(this, it)[SettingViewModel::class.java] }
        settingViewModel?.getThemeSettings()
            ?.observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                binding.switchTheme.isChecked = isDarkModeActive
                AppCompatDelegate.setDefaultNightMode(
                    if (isDarkModeActive) AppCompatDelegate.MODE_NIGHT_YES
                    else AppCompatDelegate.MODE_NIGHT_NO
                )
            }
        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel?.saveThemeSetting(isChecked)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}