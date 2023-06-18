package com.example.playlistmaker.data

import android.media.MediaPlayer
import com.example.playlistmaker.domain.MediaPlayerRepository
import com.example.playlistmaker.presentation.State

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private var playerState = State.DEFAULT
    private lateinit var mediaPlayer: MediaPlayer

    override fun preparePlayer(
        url: String,
        onStateChangedTo: (s: State) -> Unit
    ) {
        val mediaPlayer = MediaPlayer()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED
            onStateChangedTo(State.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
            onStateChangedTo(State.PREPARED)

        }
        this.mediaPlayer = mediaPlayer
    }

    override fun pause() {
        this.mediaPlayer.pause()
    }

    override fun currentPosition(): Int {
        return mediaPlayer.currentPosition
    }

    override fun switchPlayerState(onStateChangedTo: (s: State) -> Unit) {
        when (playerState) {
            State.DEFAULT -> {}
            State.PLAYING -> {
                mediaPlayer.pause()
                playerState = State.PAUSED
                onStateChangedTo(State.PAUSED)
            }
            State.PREPARED, State.PAUSED -> {
                mediaPlayer.start()
                playerState = State.PLAYING
                onStateChangedTo(State.PLAYING)
            }
        }
    }

    override fun exit() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}



