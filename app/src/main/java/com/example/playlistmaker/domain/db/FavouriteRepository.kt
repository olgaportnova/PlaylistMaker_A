package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteRepository {
    fun getAllFavouriteTracks() : Flow<List<Track>>
    fun getFavouriteIndicators() : Flow<List<Int>>
}