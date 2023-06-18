package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.State

interface AudioPlayerInteractor {

    fun preparePlayer(url: String, onStateChanged: (s: State) -> Unit)
    fun pausePlayer()
    fun currentPosition(): Int
    fun switchPlayer(onStateChangedTo: (s: State) -> Unit)

}