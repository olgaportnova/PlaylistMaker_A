package com.example.playlistmaker.domain.api

import com.example.playlistmaker.domain.model.Track


interface TrackInteractor {
    fun search(expression: String, consumer: TrackConsumer)

    interface TrackConsumer {
        fun consume(foundTracks: List<Track>?, errorMessage:String?)
    }
}