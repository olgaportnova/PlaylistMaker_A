package com.example.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.MediaPlayerRepository

class MediaPlayerRepositoryImpl (
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : MediaPlayerRepository {


    private val mediaPlayer: MediaPlayer = MediaPlayer()
    private var playerState = State.PREPARED

    override fun preparePlayer(
        url: String,
        onStateChangedTo: (s: State) -> Unit
    ) {
        mediaPlayer.setOnPreparedListener {
            playerState = State.PREPARED
            onStateChangedTo(State.PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            playerState = State.PREPARED
            onStateChangedTo(State.PREPARED)

        }
        mediaPlayer.reset()
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()


    }



     override suspend fun saveTrackToFav(track: Track) {
        var trackEntities =  trackDbConvertor.map(track)
        appDatabase.trackDao().insertTrack(trackEntities)
    }

    override suspend fun deleteTrackFromFav(track: Track) {
        val trackEntities =  trackDbConvertor.map(track)
        appDatabase.trackDao().deleteTrack(trackEntities)
    }


    override fun pause() {
        this.mediaPlayer.pause()
        playerState = State.PREPARED
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

