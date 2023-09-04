package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track

interface AudioPlayerInteractor {

    fun preparePlayer(url: String, onStateChanged: (s: State) -> Unit)
    fun pausePlayer()
    fun currentPosition(): Int
    fun switchPlayer(onStateChangedTo: (s: State) -> Unit)
    suspend fun addTrackToFav(track: Track)

    suspend fun deleteTrackFromFav(track: Track)



}