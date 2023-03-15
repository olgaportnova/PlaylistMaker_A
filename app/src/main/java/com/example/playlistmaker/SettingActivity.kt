package com.example.playlistmaker

import android.annotation.SuppressLint
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
import com.google.android.material.tabs.TabLayout.Mode

class SettingActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val switch: Switch = findViewById(R.id.theme)
        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            switch.isChecked = true
        }
        switch.setOnCheckedChangeListener() { _, isChecked ->
            if (isChecked) {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_YES)

            } else {
                AppCompatDelegate.setDefaultNightMode(MODE_NIGHT_NO)

            }

        }


        val buttonBack = findViewById<View>(R.id.back)
        buttonBack.setOnClickListener {
            finish()
        }

        val buttonSupport = findViewById<View>(R.id.support)
        buttonSupport.setOnClickListener {

            val supportIntent = Intent(Intent.ACTION_SENDTO)
            supportIntent.data = Uri.parse("mailto:")
            supportIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("yep4yep@gmail.com"))
            supportIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.message_support))
            supportIntent.putExtra(Intent.EXTRA_TEXT,getString(R.string.subject_message_support))
            startActivity(supportIntent)
        }

        val buttonShare = findViewById<View>(R.id.share)
        buttonShare.setOnClickListener {
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.putExtra(Intent.EXTRA_TEXT, getString(R.string.android_yandex))
            shareIntent.setType("text/plain");
            startActivity(shareIntent)
        }
        val buttonAgreement = findViewById<View>(R.id.agreement)
        buttonAgreement.setOnClickListener {
            val agreementIntent = Intent(Intent.ACTION_VIEW)
            agreementIntent.data = Uri.parse(getString(R.string.agreement_doc))
            startActivity(agreementIntent)
        }

    }
}