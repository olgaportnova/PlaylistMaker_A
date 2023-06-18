package com.example.playlistmaker.domain.api

import com.example.playlistmaker.data.dto.TrackDto


interface TrackInteractor {
    fun search(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundMovies: List<TrackDto>)
    }
}