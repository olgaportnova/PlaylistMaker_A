package com.example.playlistmaker

import android.app.Application
import android.content.Context

import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.main_navigation.impl.InternalNavigationRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.setting.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.history.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.domain.main_navigation.impl.InternalNavigationInteractorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.setting.settings.SettingsInteractor
import com.example.playlistmaker.domain.setting.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.setting.settings.model.ThemeSettings
import com.example.playlistmaker.domain.setting.sharing.SharingInteractor
import com.example.playlistmaker.domain.setting.sharing.impl.SharingInteractorImpl

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
    fun getAudioPlayerRepository() : MediaPlayerRepositoryImpl {
        return MediaPlayerRepositoryImpl ()
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
    }

    fun provideNavigationInteractor(context: Context): InternalNavigationInteractor {
        return InternalNavigationInteractorImpl( InternalNavigationRepositoryImpl(context))
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(
            HistoryRepositoryImpl(context)
        )
    }

    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }



}

