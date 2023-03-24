package dev.cisnux.octobycisnux.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
internal class SettingPreferences @Inject constructor(private val dataStore: DataStore<Preferences>) {
    fun getThemeSetting(): Flow<Boolean> =
        dataStore.data.map { preference ->
            preference[THEME_KEY] ?: false
        }

    suspend fun saveThemeSetting(isDarkModeActive: Boolean) {
        dataStore.edit { preference ->
            preference[THEME_KEY] = isDarkModeActive
        }
    }

    companion object {
        private val THEME_KEY = booleanPreferencesKey("theme_setting")
    }
}