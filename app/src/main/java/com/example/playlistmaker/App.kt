package com.example.playlistmaker

import android.app.Application
import android.content.Context

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.setting.SettingsInteractor
import com.example.playlistmaker.domain.setting.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.example.playlistmaker.domain.sharing.SharingInteractor
import com.example.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.MyApplication

const val APP_PREFERENCES = "my_settings"
const val DARK_THEME = "dark_theme"


class App:Application() {
    var darkTheme = false


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


//    fun switchTheme(darkThemeEnabled: Boolean) {
//        val sharedPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
//        darkTheme = darkThemeEnabled
//        AppCompatDelegate.setDefaultNightMode(
//            if (darkThemeEnabled) {
//                sharedPref.edit()
//                    .putBoolean(DARK_THEME, true)
//                    .apply()
//                AppCompatDelegate.MODE_NIGHT_YES
//            } else {
//                sharedPref.edit()
//                    .putBoolean(DARK_THEME, false)
//                    .apply()
//                AppCompatDelegate.MODE_NIGHT_NO
//            }
//        )
//    }

    // setting part
    fun getSettingRepository(context: Context): SettingsRepositoryImpl {
        return SettingsRepositoryImpl(context)
    }

    fun provideSettingInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(getSettingRepository(context))
    }


    // sharing part
    fun getSharingRepository(context: Context): ExternalNavigationImpl {
        return ExternalNavigationImpl(context)
    }

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(getSharingRepository(context))
    }


}

