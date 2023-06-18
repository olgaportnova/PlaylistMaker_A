package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.api.TrackInteractor
import java.util.concurrent.Executors

class TrackInteractorImpl(private val repository: TrackRepository) : TrackInteractor {

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: TrackInteractor.TrackConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}