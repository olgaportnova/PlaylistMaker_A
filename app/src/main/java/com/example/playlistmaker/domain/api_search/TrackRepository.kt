package com.example.playlistmaker.domain.api_search

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource
import kotlinx.coroutines.flow.Flow


interface TrackRepository {
    fun search(expression: String): Flow<Resource<List<Track>>>

    fun getFavIndicators() : Flow<List<Int>>
}