package com.example.playlistmaker.domain.db

import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavouriteInteractor {

    fun getAllFavouriteTracks() : Flow<List<Track>>
    fun getIdOfFavouriteTracks() : Flow<List<Int>>
}