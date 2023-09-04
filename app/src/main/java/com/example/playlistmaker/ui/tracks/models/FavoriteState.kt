package com.example.playlistmaker.ui.tracks.models

import com.example.playlistmaker.domain.model.Track

sealed interface FavoriteState {

    object Loading : FavoriteState

    data class Content(
        val tracks: List<Track>
    ) : FavoriteState

    data class Empty(
        val message: String
    ) : FavoriteState
}