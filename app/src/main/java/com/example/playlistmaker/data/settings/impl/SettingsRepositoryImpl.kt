package com.example.playlistmaker.data.settings.impl

import android.content.Context
import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.setting.model.ThemeSettings


private const val SHARED_PREFS_NAME = "shared_prefs_name"
private const val DARK_MODE = "dark_mode"

class SettingsRepositoryImpl(context: Context) : SettingsRepository {


    private val sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

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