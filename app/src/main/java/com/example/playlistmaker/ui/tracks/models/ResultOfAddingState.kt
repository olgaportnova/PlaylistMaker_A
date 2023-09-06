package com.example.playlistmaker.ui.tracks.models

import com.example.playlistmaker.domain.model.Playlist

        sealed interface ResultOfAddingState {

            data class SUCCESS(
                val playlists: Playlist
            ) : ResultOfAddingState

            data class ALREADY_EXISTS(
                val playlists: Playlist
            ) : ResultOfAddingState

        }