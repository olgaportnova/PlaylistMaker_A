package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.playlistmaker.creator.Creator
import com.example.playlistmaker.databinding.ActivityMainBinding
import com.example.playlistmaker.databinding.ActivitySettingsBinding
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.presentation.LibraryActivity
import com.example.playlistmaker.presentation.SettingActivity


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttonSettings = findViewById<View>(R.id.settings)
        val buttonLibrary = findViewById<View>(R.id.library)
        val buttonSearch = findViewById<View>(R.id.search)


        val internalNavigationInteractor = Creator.provideNavigationInteractor(applicationContext)


        buttonSettings.setOnClickListener { internalNavigationInteractor.toSettingsScreen() }
        buttonLibrary.setOnClickListener { internalNavigationInteractor.toLibraryScreen() }
        buttonSearch.setOnClickListener { internalNavigationInteractor.toSearchScreen() }

    }
}

