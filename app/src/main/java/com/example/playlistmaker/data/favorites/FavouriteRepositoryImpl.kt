package com.example.playlistmaker.data.favorites

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.TrackDbConvertor
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.domain.db.FavouriteRepository
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavouriteRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : FavouriteRepository {

    override fun favouriteTracks(): Flow<List<Track>>  = flow {
       val tracks = appDatabase.trackDao().getAllFavouriteTracks()
        emit(convertFromTrackEntity(tracks))
    }

    private fun convertFromTrackEntity(tracks: List<TrackEntity>): List<Track> {
        return tracks.map { track -> trackDbConvertor.map(track) }
    }

    override fun getFavIndicators() : Flow<List<Int>> = flow {
        val listOfIdFavTracks = appDatabase.trackDao().getIdsOfFavouriteTracks()
        emit(listOfIdFavTracks)
    }











}