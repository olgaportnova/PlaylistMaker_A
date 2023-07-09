package com.example.playlistmaker.ui.tracks.models

import com.example.playlistmaker.domain.model.Track

data class TracksState(

    val tracks: List<Track>,
    val isLoading: Boolean,
    val placeholderMessage: Int?,
    val needToUpdate:Boolean,
    val toShowHistory:Boolean,
    val history: List<Track>

)
