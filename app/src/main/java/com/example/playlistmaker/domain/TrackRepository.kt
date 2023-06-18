package com.example.playlistmaker.domain

import com.example.playlistmaker.data.dto.TrackDto


interface TrackRepository {
    fun search(expression: String): List<TrackDto>
}