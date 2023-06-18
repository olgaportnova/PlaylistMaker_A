package com.example.playlistmaker.domain

import android.media.MediaPlayer
import com.example.playlistmaker.presentation.State

interface MediaPlayerRepository {

    fun preparePlayer(url: String, onStateChangedTo: (s: State) -> Unit)

    fun currentPosition(): Int

    fun pause()

    fun switchPlayerState(onStateChangedTo: (s: State) -> Unit)

    fun exit()
}