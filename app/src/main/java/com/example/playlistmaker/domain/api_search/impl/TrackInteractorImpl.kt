package com.example.playlistmaker.domain.api_search.impl

import com.example.playlistmaker.domain.api_search.TrackRepository
import com.example.playlistmaker.domain.api_search.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {


    override fun search(expression: String): Flow<Pair<List<Track>?,String?>> {
        return repository.search(expression).map { result ->
            when(result) {
                is Resource.Success -> {
                    Pair(result.data,null)
                }
                is Resource.Error -> {
                    Pair(null, result.message)
                }
            }
        }
    }

    override fun loadTracks(onComplete: (Boolean) -> Unit) {
        onComplete(true)
        }

    override fun getFavIndicators(): Flow<List<Int>> {
       return repository.getFavIndicators()
    }


}


