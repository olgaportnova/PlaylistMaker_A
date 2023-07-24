package com.example.playlistmaker.data.setting.settings.impl

import android.content.SharedPreferences
import com.example.playlistmaker.data.setting.settings.SettingsRepository
import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings


private const val SHARED_PREFS_DARK_MODE = "shared_prefs_dark_mode"
private const val DARK_MODE = "dark_mode"

class SettingsRepositoryImpl(private val sharedPreferences: SharedPreferences) : SettingsRepository {

    override fun getThemeSettings(): ThemeSettings {
        val intMode = sharedPreferences.getInt(DARK_MODE, -1 )
        return fromInt(intMode) ?: ThemeSettings.MODE_DARK_NO


    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPreferences.edit().putInt(DARK_MODE, settings.intMode).apply()
    }

    companion object {
        private val map = ThemeSettings.values().associateBy(ThemeSettings::intMode)
        fun fromInt(type: Int) = map[type]
    }
}