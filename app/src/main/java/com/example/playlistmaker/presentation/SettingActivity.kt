package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.playlistmaker.R
import com.example.playlistmaker.creator.Creator.provideSettingsInteractor
import com.example.playlistmaker.creator.Creator.provideSharingInteractor
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.setting.model.ThemeSettings

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