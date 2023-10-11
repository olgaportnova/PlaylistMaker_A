package com.example.playlistmaker.presentation.playlistDetails

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track

sealed interface TracksInPlaylistState {

    object Loading : TracksInPlaylistState

    object Empty : TracksInPlaylistState

    data class Content(
        val tracks: List<Track>,
        val trackDurations : Int
    ) : TracksInPlaylistState

}