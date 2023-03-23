package dev.cisnux.octobycisnux.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import dev.cisnux.octobycisnux.repository.UserRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: UserRepository) : ViewModel() {
    val userThemePreferences: LiveData<Boolean> get() = repository.themeSetting.asLiveData()

    fun saveThemePreferences(isDarkMode: Boolean) = viewModelScope.launch {
        repository.saveThemeSetting(isDarkMode)
    }
}