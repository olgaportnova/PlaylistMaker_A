

package com.example.playlistmaker.domain.player

import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track

interface MediaPlayerRepository {

    fun preparePlayer(url: String, onStateChangedTo: (s: State) -> Unit)

    fun currentPosition(): Int

    fun pause()

    fun switchPlayerState(onStateChangedTo: (s: State) -> Unit)

    suspend fun saveTrackToFav(track: Track)

    suspend fun deleteTrackFromFav(track: Track)


    fun exit()
}





