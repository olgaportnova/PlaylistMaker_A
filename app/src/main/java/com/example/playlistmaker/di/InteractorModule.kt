package com.example.playlistmaker.di

import android.media.MediaPlayer
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.db.FavouriteInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.history.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.FavouriteInteractorImpl
import com.example.playlistmaker.domain.impl.PlaylistInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.domain.main_navigation.impl.InternalNavigationInteractorImpl
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.setting.settings.SettingsInteractor
import com.example.playlistmaker.domain.setting.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.setting.sharing.SharingInteractor
import com.example.playlistmaker.domain.setting.sharing.impl.SharingInteractorImpl
import com.google.gson.Gson
import org.koin.dsl.module


    val interactorModule = module {

        single<FavouriteInteractor> {
            FavouriteInteractorImpl(get())
        }

        single<PlaylistInteractor> {
            PlaylistInteractorImpl(get())
        }

        single<TrackInteractor> {
            TrackInteractorImpl(get())
        }

        single<HistoryInteractor> {
            HistoryInteractorImpl(get(),get())
        }

        single<InternalNavigationInteractor> {
            InternalNavigationInteractorImpl(get())
        }

        single<AudioPlayerInteractor> {
            AudioPlayerInteractorImpl(get())
        }

        single<SettingsInteractor> {
            SettingsInteractorImpl(get())
        }

        single<SharingInteractor> {
            SharingInteractorImpl(get())
        }

        factory { Gson() }



    }
