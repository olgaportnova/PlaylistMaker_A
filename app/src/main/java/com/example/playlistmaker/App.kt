package com.example.playlistmaker

import android.app.Application

import androidx.appcompat.app.AppCompatDelegate

const val APP_PREFERENCES = "my_settings"
const val DARK_THEME = "dark_theme"


class App:Application() {
    var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPref.getBoolean(DARK_THEME, false)
        switchTheme(darkTheme)
    }


        fun switchTheme(darkThemeEnabled: Boolean) {
            val sharedPref = getSharedPreferences(APP_PREFERENCES, MODE_PRIVATE)
            darkTheme = darkThemeEnabled
            AppCompatDelegate.setDefaultNightMode(
                if (darkThemeEnabled) {
                    sharedPref.edit()
                        .putBoolean(DARK_THEME, true)
                        .apply()
                    AppCompatDelegate.MODE_NIGHT_YES
                } else {
                    sharedPref.edit()
                        .putBoolean(DARK_THEME, false)
                        .apply()
                    AppCompatDelegate.MODE_NIGHT_NO
                }
            )
        }
    }

