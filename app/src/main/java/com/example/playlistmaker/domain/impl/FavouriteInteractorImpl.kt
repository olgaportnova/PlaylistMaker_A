package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.db.FavouriteInteractor
import com.example.playlistmaker.domain.db.FavouriteRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavouriteInteractorImpl(
    private val favouriteRepository: FavouriteRepository
): FavouriteInteractor {
    override fun getAllFavouriteTracks(): Flow<List<Track>> {
        return favouriteRepository.getAllFavouriteTracks()
    }

    override fun getIdOfFavouriteTracks(): Flow<List<Int>> {
        return favouriteRepository.getFavouriteIndicators()
    }

}