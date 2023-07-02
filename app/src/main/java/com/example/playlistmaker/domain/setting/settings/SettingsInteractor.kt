package com.example.playlistmaker.domain.setting.settings

import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings

interface SettingsInteractor {
    fun getThemeSettings(): ThemeSettings
    fun updateThemeSetting(settings: ThemeSettings)
}