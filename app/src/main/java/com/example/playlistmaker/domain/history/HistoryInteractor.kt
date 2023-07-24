package com.example.playlistmaker.domain.history

import com.example.playlistmaker.domain.model.Track


interface HistoryInteractor {


    fun addTrackToHistory(track: Track)


    fun getHistoryString(): String?

    fun clearHistory(): Unit

    fun updateHistory(updatedHistoryList :ArrayList<Track>) :Unit

    fun getHistoryList() : ArrayList<Track>
}