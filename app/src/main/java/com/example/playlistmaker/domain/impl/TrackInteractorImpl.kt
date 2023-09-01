package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.concurrent.Executors

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
    }


