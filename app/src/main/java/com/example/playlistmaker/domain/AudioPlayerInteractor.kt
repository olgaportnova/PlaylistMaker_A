package com.example.playlistmaker.domain

import com.example.playlistmaker.presentation.State

class AudioPlayerInteractor(
    private val mediaPlayerRepository: MediaPlayerRepository
) {

    fun preparePlayer(
        url: String,
        onStateChanged: (s: State) -> Unit
    ) {
        mediaPlayerRepository.preparePlayer(
            url,
            onStateChanged
        )
    }

    fun pausePlayer() {
        mediaPlayerRepository.pause()
    }

    fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    fun switchPlayer(onStateChangedTo: (s: State) -> Unit) {
        mediaPlayerRepository.switchPlayerState(onStateChangedTo)
    }


}