package com.ssamsara98.dicodingevents.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.ssamsara98.dicodingevents.databinding.FragmentSettingBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SettingFragment : Fragment() {

    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!! // This property is only valid between onCreateView and onDestroyView.

    // private val settingViewModel: SettingViewModel? by viewModels {
    //     activity?.let { ViewModelFactory.getInstance(it) }!!
    // }
    private val settingViewModel: SettingViewModel? by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(inflater, container, false)
        val root: View = binding.root

        settingViewModel?.apply {
            this.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
                binding.switchTheme.isChecked = isDarkModeActive
            }

            binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
                this.saveThemeSetting(isChecked)
            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}