package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun favouriteTracks() : Flow<List<Track>>
    fun getFavIndicators() : Flow<List<Int>>
}