package com.example.playlistmaker.util

import android.app.Application

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings
import com.example.playlistmaker.util.Creator.provideSettingInteractor


class App:Application() {

    override fun onCreate() {

        // достаем из SH dark mode
        val settingsInteractor = provideSettingInteractor(applicationContext)
        darkMode(settingsInteractor.getThemeSettings())

        super.onCreate()
    }

    // устанавливаем dark Mode
    private fun darkMode(isDarkMode: ThemeSettings) {
        if (isDarkMode == ThemeSettings.MODE_DARK_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

}




