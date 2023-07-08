package com.example.playlistmaker.data.history.impl

import android.content.Context
import android.content.Intent
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerActivity


private const val SHARED_PREFS_NAME = "shared_prefs_name"
const val SEARCH_HISTORY = "search_history"
const val TRACK_LIST_KEY = "track_list_key"
const val TRACK_TO_OPEN = "item"
class HistoryRepositoryImpl(context: Context) : HistoryRepository {

    var context = context

    private var sharedPref = context.getSharedPreferences(SEARCH_HISTORY, Context.MODE_PRIVATE)
    private var sharedPreferences = context.getSharedPreferences(SHARED_PREFS_NAME, Context.MODE_PRIVATE)

    override fun updateTrackHistory(updatedHistory:String) {
        sharedPref.edit().
        putString(TRACK_LIST_KEY, updatedHistory)
            .apply()
    }


    override fun getHistoryString():String{
        return sharedPref.getString(TRACK_LIST_KEY, null) ?: ""
    }

    override fun clearHistory() {
        sharedPref.edit().remove(TRACK_LIST_KEY).apply()
    }

    override fun openTrack(track: Track) {
        val displayIntent = Intent(context, AudioPlayerActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        displayIntent.putExtra(TRACK_TO_OPEN, track)
        context.startActivity(displayIntent)
    }


}