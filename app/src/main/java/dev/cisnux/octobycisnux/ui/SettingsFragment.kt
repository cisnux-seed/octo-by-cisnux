package dev.cisnux.octobycisnux.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import dagger.hilt.android.AndroidEntryPoint
import dev.cisnux.octobycisnux.R
import dev.cisnux.octobycisnux.databinding.FragmentSettingsBinding
import dev.cisnux.octobycisnux.viewmodels.SettingsViewModel

@AndroidEntryPoint
class SettingsFragment : Fragment() {
    private val viewModel: SettingsViewModel by activityViewModels()
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.userThemePreferences.observe(viewLifecycleOwner, ::setSwitchTheme)
        binding.switchTheme.setOnCheckedChangeListener { _, isChecked ->
            viewModel.saveThemePreferences(isChecked)
        }
    }

    private fun setSwitchTheme(isDarkModeActive: Boolean) = with(binding) {
        switchTheme.isChecked = isDarkModeActive
        switchTheme.text =
            getString(
                if (!isDarkModeActive) R.string.summary_dark_mode
                else R.string.summary_light_mode
            )
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}