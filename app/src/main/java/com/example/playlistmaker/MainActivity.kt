package com.example.playlistmaker

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.content.Intent
import com.example.playlistmaker.presentation.LibraryActivity
import com.example.playlistmaker.presentation.SettingActivity


class MainActivity : AppCompatActivity() {


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        if (darkTheme) {
//            AppCompatDelegate.MODE_NIGHT_YES
//        }


        val buttonSettings=findViewById<View>(R.id.settings)
        buttonSettings.setOnClickListener {
            val displayIntent = Intent(this, SettingActivity::class.java)
            startActivity(displayIntent)
        }
        val buttonLibrary=findViewById<View>(R.id.library)
        buttonLibrary.setOnClickListener {
            val displayIntent = Intent(this, LibraryActivity::class.java)
            startActivity(displayIntent)
        }
        val buttonSearch=findViewById<View>(R.id.search)
        buttonSearch.setOnClickListener {
            val displayIntent = Intent(this, SearchActivity::class.java)
            startActivity(displayIntent)
        }


    }
}

