package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import com.example.playlistmaker.domain.model.State

class MediaPlayerRepositoryImpl : MediaPlayerRepository {

    private var playerState = State.PREPARED
    private lateinit var mediaPlayer: MediaPlayer

    override fun preparePlayer(
        url: String,
        onStateChangedTo: (s: State) -> Unit
    ) {
        var mediaPlayer = MediaPlayer()
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

            State.PREPARED, State.PAUSED -> {
                mediaPlayer.start()
                playerState = State.PLAYING
                onStateChangedTo(State.PLAYING)

            }
            State.PLAYING -> {
                mediaPlayer.pause()
                playerState = State.PAUSED
                onStateChangedTo(State.PAUSED)

            }
        }
    }




    override fun exit() {
        mediaPlayer.stop()
        mediaPlayer.release()
    }
}

