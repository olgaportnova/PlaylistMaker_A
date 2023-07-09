package com.example.playlistmaker.util

import android.app.Application
import android.content.Context
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.setting.settings.SettingsInteractor
import com.example.playlistmaker.domain.setting.settings.impl.SettingsInteractorImpl

class MyApplication: Application() {
//    fun getRepository(): TrackRepositoryImpl {
//        return TrackRepositoryImpl(RetrofitNetworkClient(this))
//    }
//
//    fun provideTracksInteractor(): TrackInteractor {
//        return TrackInteractorImpl(getRepository())
//    }
//
//    fun getRepository(context: Context) : SettingsRepositoryImpl {
//        return SettingsRepositoryImpl(context)
//    }
//
//    fun provideSettingInteractor(context: Context): SettingsInteractor {
//        return SettingsInteractorImpl(getRepository(context))
//    }
//
//    fun getAudioPlayerRepository() : MediaPlayerRepositoryImpl {
//        return MediaPlayerRepositoryImpl ()
//    }
//
//    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
//        return AudioPlayerInteractorImpl(getAudioPlayerRepository())
//    }

}