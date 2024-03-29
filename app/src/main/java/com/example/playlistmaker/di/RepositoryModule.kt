package com.example.playlistmaker.di

import android.content.Context
import com.example.playlistmaker.data.favorites.FavouriteRepositoryImpl
import com.example.playlistmaker.data.network.TrackRepositoryImpl
import com.example.playlistmaker.data.db.convertors.TrackDbConvertor
import com.example.playlistmaker.data.db.convertors.PlaylistDbConvertor
import com.example.playlistmaker.data.db.convertors.TrackInPlaylistsEntityDbConvertor
import com.example.playlistmaker.data.playlists.PlaylistRepositoryImpl
import com.example.playlistmaker.data.history.HistoryRepository
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.main_navigation.InternalNavigationRepository
import com.example.playlistmaker.data.main_navigation.impl.InternalNavigationRepositoryImpl
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.setting.settings.SettingsRepository
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.setting.sharing.ExternalNavigator
import com.example.playlistmaker.data.setting.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.api_search.TrackRepository
import com.example.playlistmaker.domain.favourite.FavouriteRepository
import com.example.playlistmaker.domain.playlists.PlaylistRepository
import com.example.playlistmaker.domain.player.MediaPlayerRepository
import org.koin.android.ext.koin.androidContext
import org.koin.core.qualifier.named
import org.koin.dsl.module


val repositoryModule = module {

        single<FavouriteRepository> {
            FavouriteRepositoryImpl(get(), get())
        }

        single<PlaylistRepository> {
            PlaylistRepositoryImpl(get(), get(), get(),get())
        }

        factory { TrackDbConvertor() }

        factory { PlaylistDbConvertor() }

        single { get<Context>().contentResolver }

        factory { TrackInPlaylistsEntityDbConvertor() }

        single<TrackRepository> {
            TrackRepositoryImpl(get(),get())
        }

        single<InternalNavigationRepository> {
            InternalNavigationRepositoryImpl(androidContext())
        }

        single<MediaPlayerRepository> {
            MediaPlayerRepositoryImpl(get(),get())
        }

        single<ExternalNavigator> {
            ExternalNavigationImpl(androidContext())
        }

        single<SettingsRepository> {
            SettingsRepositoryImpl(get(named(SHARED_PREFS_DARK_MODE)))
        }

        single<HistoryRepository> {
            HistoryRepositoryImpl(get(named(SHARED_PREFS_SEARCH_HISTORY)),get())
        }

    }
