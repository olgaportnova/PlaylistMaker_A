package com.example.playlistmaker.domain.setting.settings.impl

import com.example.playlistmaker.data.setting.settings.SettingsRepository
import com.example.playlistmaker.domain.setting.settings.SettingsInteractor
import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings

class SettingsInteractorImpl(  private val settingRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingRepository.updateThemeSetting(settings)
    }
}