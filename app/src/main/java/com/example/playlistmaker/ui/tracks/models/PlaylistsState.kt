package com.example.playlistmaker.ui.tracks.models

import com.example.playlistmaker.domain.model.Playlist


sealed interface PlaylistsState {

    object Loading : PlaylistsState

    object Empty : PlaylistsState


    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

}