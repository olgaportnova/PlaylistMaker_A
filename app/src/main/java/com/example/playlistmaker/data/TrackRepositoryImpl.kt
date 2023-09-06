package com.example.playlistmaker.data

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val ERROR_NO_CONNECTION_TO_INTERNET = -1
const val SEARCH_SUCCESS = 200

class TrackRepositoryImpl(
    private val networkClient: NetworkClient,
    private val appDatabase: AppDatabase,

    ) : TrackRepository {

    override fun search(expression: String): Flow<Resource<List<Track>>> = flow {
        val response = networkClient.getTracksFromItunes(TrackSearchRequest(expression))
        when (response.resultCode) {
            ERROR_NO_CONNECTION_TO_INTERNET -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }

            SEARCH_SUCCESS -> {
                with(response as TrackSearchResponse) {
                    val data = results.map {
                        Track(
                            it.trackName,
                            it.artistName,
                            it.trackTimeMillis,
                            it.artworkUrl100,
                            it.trackId,
                            it.collectionName,
                            it.releaseDate,
                            it.primaryGenreName,
                            it.country,
                            it.previewUrl
                        )
                    }
                    val listOfIdFavTracks = appDatabase.trackDao().getIdsOfFavouriteTracks()
                    setFavIndicator(data, listOfIdFavTracks)
                    emit(Resource.Success(data))
                }
            }

            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }

    override fun getFavIndicators(): Flow<List<Int>> = flow {
        val listOfIdFavTracks = appDatabase.trackDao().getIdsOfFavouriteTracks()
        emit(listOfIdFavTracks)
    }

    private fun setFavIndicator(data: List<Track>, listOfIdFavTracks: List<Int>) {
        for (i in data) {
            if (i.trackId in listOfIdFavTracks) {
                i.isFavorite = true
            }
        }


    }
}