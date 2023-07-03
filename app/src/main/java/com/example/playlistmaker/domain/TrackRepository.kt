package com.example.playlistmaker.domain

import com.example.playlistmaker.domain.model.Track


interface TrackRepository {
    fun search(expression: String): List<Track>
}