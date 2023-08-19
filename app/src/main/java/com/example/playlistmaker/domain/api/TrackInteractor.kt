package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow


interface TrackInteractor {
    fun search(expression: String): Flow<Pair<List<Track>?, String?>>


    fun loadTracks(onComplete : (Boolean) -> Unit)
}