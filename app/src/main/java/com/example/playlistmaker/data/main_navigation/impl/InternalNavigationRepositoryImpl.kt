package com.example.playlistmaker.data.main_navigation.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.history.impl.TRACK_TO_OPEN
import com.example.playlistmaker.data.main_navigation.InternalNavigationRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerActivity


class InternalNavigationRepositoryImpl(private val context: Context): InternalNavigationRepository {
//    override fun toSettingsScreen() {
//        val displayIntent = Intent(context, SettingActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(displayIntent)
//    }
//
//    override fun toLibraryScreen() {
//        val displayIntent = Intent(context, LibraryActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(displayIntent)
//    }
//
//    override fun toSearchScreen() {
//        val displayIntent = Intent(context, SearchActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
//        context.startActivity(displayIntent)
//    }
    override fun openTrack(track: Track) {
        val displayIntent = Intent(context, AudioPlayerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        displayIntent.putExtra(TRACK_TO_OPEN, track)
        context.startActivity(displayIntent)
    }

}