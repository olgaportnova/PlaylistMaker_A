package com.example.playlistmaker

import com.example.playlistmaker.data.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.domain.AudioPlayerInteractor
import com.example.playlistmaker.domain.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.impl.TrackInteractorImpl

object Creator {

    fun provideTrackInteractor(): TrackInteractor {
        return TrackInteractorImpl(
            TrackRepositoryImpl(RetrofitNetworkClient())
        )
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            MediaPlayerRepositoryImpl()
        )
    }
}