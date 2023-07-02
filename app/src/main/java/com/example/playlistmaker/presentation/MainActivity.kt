package com.example.playlistmaker.presentation

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.databinding.ActivityMainBinding


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

