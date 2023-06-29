

package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.State

interface MediaPlayerRepository {

    fun preparePlayer(url: String, onStateChangedTo: (s: State) -> Unit)

    fun currentPosition(): Int

    fun pause()

    fun switchPlayerState(onStateChangedTo: (s: State) -> Unit)

    fun exit()
}





