package com.example.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_NO
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_NO
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES
import com.example.playlistmaker.Creator.provideSettingsInteractor
import com.example.playlistmaker.Creator.provideSharingInteractor
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.setting.model.ThemeSettings
import com.google.android.material.tabs.TabLayout.Mode

class SettingActivity() : AppCompatActivity() {


    private lateinit var context: Context
    private lateinit var binding: ActivitySettingsBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        context = getApplicationContext()
        val sharingInteractor = provideSharingInteractor(this.applicationContext)
        val settingsInteractor = provideSettingsInteractor(this.applicationContext)


        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)


        // settings part
        if (settingsInteractor.getThemeSettings()== ThemeSettings.MODE_DARK_YES) {
            binding.theme.isChecked = true
        }

        binding.theme.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                settingsInteractor.updateThemeSetting(ThemeSettings.MODE_DARK_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                settingsInteractor.updateThemeSetting(ThemeSettings.MODE_DARK_NO)
            }
        }


        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            finish()
        }



        // sharing part

        binding.support.setOnClickListener {
            sharingInteractor.openSupport()
        }

        binding.share.setOnClickListener {
            sharingInteractor.shareApp()
        }

        binding.agreement.setOnClickListener {
            sharingInteractor.openTerms()
        }

    }
}