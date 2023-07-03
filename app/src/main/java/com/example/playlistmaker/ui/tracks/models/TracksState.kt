package com.example.playlistmaker.ui.tracks.models

import com.example.playlistmaker.domain.model.Track

data class TracksState(

    val tracks: List<Track>,
    val isLoading: Boolean,
    val placeholderMessage: String?,
    val placeholderImage: Int?,
    val needToUpdate:Boolean

)
