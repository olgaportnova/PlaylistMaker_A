package com.example.playlistmaker.presentation.settings

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivitySettingsBinding



class SettingActivity : AppCompatActivity() {



    private lateinit var context: Context
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var viewModel: SettingViewModel

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        context = applicationContext



        viewModel = ViewModelProvider(this, SettingViewModel.getViewModelFactory())[SettingViewModel::class.java]

        viewModel.getModeLiveData().observe(this) {isDarkMode ->
            changeMode(isDarkMode)
        }

        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.theme.setOnCheckedChangeListener { buttonView, isChecked ->
            viewModel.changeMode(isChecked) }


        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            finish()
        }



        // sharing part

        binding.support.setOnClickListener {
            viewModel.openSupport(getString(R.string.subject_message_support),getString(R.string.message_support))
        }

        binding.share.setOnClickListener {
            viewModel.shareApp()
        }

        binding.agreement.setOnClickListener {
            viewModel.legalAgreement()
        }

    }

    private fun changeMode (isDarkMode:Boolean) {
            if(isDarkMode==true) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.theme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.theme.isChecked = false
            }
    }
}