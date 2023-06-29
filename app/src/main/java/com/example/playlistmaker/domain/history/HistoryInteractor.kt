package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.model.Track


interface HistoryInteractor {

    fun addTrackToHistory(track: Track)

//    fun getHistory(): ArrayList<Track>

    fun getHistoryString(): String?

    fun clearHistory(): Unit

    fun updateHistory(updatedHistoryList :ArrayList<Track>) :Unit

    fun openTrack(track: Track)
}