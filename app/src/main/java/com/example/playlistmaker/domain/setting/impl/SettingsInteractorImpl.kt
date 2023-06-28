package com.example.playlistmaker.domain.setting.impl

import com.example.playlistmaker.data.settings.SettingsRepository
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.model.ThemeSettings

class SettingsInteractorImpl(  private val settingRepository: SettingsRepository
) : SettingsInteractor {
    override fun getThemeSettings(): ThemeSettings {
        return settingRepository.getThemeSettings()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        settingRepository.updateThemeSetting(settings)
    }
}