

package com.example.playlistmaker.domain.player.impl

import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.State

class AudioPlayerInteractorImpl(
    private val mediaPlayerRepository: MediaPlayerRepository
) : AudioPlayerInteractor {

    override fun preparePlayer(url: String, onStateChanged: (s: State) -> Unit) {
        mediaPlayerRepository.preparePlayer(
            url,
            onStateChanged
        )
    }

    override fun pausePlayer() {
        mediaPlayerRepository.pause()
    }

    override fun currentPosition(): Int {
        return mediaPlayerRepository.currentPosition()
    }

    override fun switchPlayer(onStateChangedTo: (s: State) -> Unit) {
        mediaPlayerRepository.switchPlayerState(onStateChangedTo)
    }


}



