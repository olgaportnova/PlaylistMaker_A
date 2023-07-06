package com.example.playlistmaker.presentation.tracks

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState


interface TracksView {

    fun updatedViewBasedOnStatus (state:TracksState)

}