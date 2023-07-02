package com.example.playlistmaker.data.setting.settings

import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings

interface SettingsRepository {

    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}