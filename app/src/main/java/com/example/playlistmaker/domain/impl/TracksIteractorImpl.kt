package com.example.playlistmaker.domain.impl

import com.example.playlistmaker.domain.api.TracksRepository
import com.example.playlistmaker.domain.models.TracksInteractor
import java.util.concurrent.Executors

class TracksInteractorImpl(private val repository: TracksRepository) : TracksInteractor{

    private val executor = Executors.newCachedThreadPool()

    override fun search(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            consumer.consume(repository.search(expression))
        }
    }
}