package com.vb.kanjimap_android.core.datastore

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.vb.kanjimap_android.core.common.Constants
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.settingsDataStore by preferencesDataStore(name = Constants.SETTINGS_DATASTORE_NAME)

class SettingsDataStore(
    private val context: Context
) {
    private object Keys {
        val isOnboardingCompleted = booleanPreferencesKey("is_onboarding_completed")
        val darkThemeEnabled = booleanPreferencesKey("dark_theme_enabled")
    }

    val isOnboardingCompleted: Flow<Boolean> =
        context.settingsDataStore.data.map { prefs -> prefs[Keys.isOnboardingCompleted] ?: false }

    val darkThemeEnabled: Flow<Boolean> =
        context.settingsDataStore.data.map { prefs -> prefs[Keys.darkThemeEnabled] ?: false }

    suspend fun setOnboardingCompleted(isCompleted: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.isOnboardingCompleted] = isCompleted
        }
    }

    suspend fun setDarkThemeEnabled(isEnabled: Boolean) {
        context.settingsDataStore.edit { prefs ->
            prefs[Keys.darkThemeEnabled] = isEnabled
        }
    }
}
