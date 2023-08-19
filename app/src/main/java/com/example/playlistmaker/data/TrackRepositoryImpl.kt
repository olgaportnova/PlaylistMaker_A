package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.TrackSearchRequest
import com.example.playlistmaker.data.dto.TrackSearchResponse
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

const val ERROR_NO_CONNECTION_TO_INTERNET =-1
const val SEARCH_SUCCESS =200

class TrackRepositoryImpl(private val networkClient: NetworkClient) : TrackRepository {

    override fun search(expression: String): Flow<Resource<List<Track>>> = flow{
        val response = networkClient.getTracksFromItunes(TrackSearchRequest(expression))
        when (response.resultCode) {
            ERROR_NO_CONNECTION_TO_INTERNET -> {
                emit(Resource.Error("Проверьте подключение к интернету"))
            }
            SEARCH_SUCCESS-> {
                with (response as TrackSearchResponse) {
                val data = results.map{
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
                    )}
                    emit(Resource.Success(data))
                }
            }
            else -> {
                emit(Resource.Error("Ошибка сервера"))
            }
        }
    }
}