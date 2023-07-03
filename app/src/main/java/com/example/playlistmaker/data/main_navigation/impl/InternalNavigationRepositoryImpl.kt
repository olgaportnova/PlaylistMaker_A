package com.example.playlistmaker.data.main_navigation.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.presentation.search.SearchActivity
import com.example.playlistmaker.data.main_navigation.InternalNavigationRepository
import com.example.playlistmaker.presentation.library.LibraryActivity
import com.example.playlistmaker.presentation.settings.SettingActivity

class InternalNavigationRepositoryImpl(private val context: Context): InternalNavigationRepository {
    override fun toSettingsScreen() {
        val displayIntent = Intent(context, SettingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }

    override fun toLibraryScreen() {
        val displayIntent = Intent(context, LibraryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }

    override fun toSearchScreen() {
        val displayIntent = Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(displayIntent)
    }

}