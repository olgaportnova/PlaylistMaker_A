package com.example.playlistmaker.data.history

import android.content.Context
import com.example.playlistmaker.domain.model.Track

interface HistoryRepository {

    fun updateTrackHistory(tracksHistory: String)
    fun getHistoryString() : String
    fun clearHistory(): Unit
    fun openTrack( track: Track)
}