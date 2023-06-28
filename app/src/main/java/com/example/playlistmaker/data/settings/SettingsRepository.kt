package com.example.playlistmaker.data.settings

import com.example.playlistmaker.domain.setting.model.ThemeSettings

interface SettingsRepository {

    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}