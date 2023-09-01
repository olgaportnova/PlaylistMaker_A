package com.example.playlistmaker.di

import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.main_navigation.InternalNavigationRepository
import com.example.playlistmaker.data.main_navigation.impl.InternalNavigationRepositoryImpl
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.setting.settings.SettingsRepository
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.setting.sharing.ExternalNavigator
import com.example.playlistmaker.data.setting.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repositoryModule = module {

        single<TrackRepository> {
            TrackRepositoryImpl(get())
        }

        single<InternalNavigationRepository> {
            InternalNavigationRepositoryImpl(androidContext())
        }

        single<MediaPlayerRepository> {
            MediaPlayerRepositoryImpl()
        }

        single<ExternalNavigator> {
            ExternalNavigationImpl(androidContext())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(named(SHARED_PREFS_DARK_MODE)))
        }

        single<HistoryRepository> {
            HistoryRepositoryImpl(get(named(SHARED_PREFS_SEARCH_HISTORY)))
        }

    }
