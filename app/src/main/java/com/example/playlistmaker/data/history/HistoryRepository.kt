package com.example.playlistmaker.data.history

interface HistoryRepository {

    fun updateTrackHistory(tracksHistory: String)
    fun getHistoryString() : String
    fun clearHistory(): Unit

}