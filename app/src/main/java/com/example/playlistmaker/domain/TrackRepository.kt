package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.util.Resource


interface TrackRepository {
    fun search(expression: String): Resource<List<Track>>
}