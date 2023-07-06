package com.example.playlistmaker.util

import android.content.Context
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.data.player.impl.MediaPlayerRepositoryImpl
import com.example.playlistmaker.data.TrackRepositoryImpl
import com.example.playlistmaker.data.history.impl.HistoryRepositoryImpl
import com.example.playlistmaker.data.main_navigation.impl.InternalNavigationRepositoryImpl
import com.example.playlistmaker.data.network.RetrofitNetworkClient
import com.example.playlistmaker.data.setting.settings.impl.SettingsRepositoryImpl
import com.example.playlistmaker.data.setting.sharing.impl.ExternalNavigationImpl
import com.example.playlistmaker.domain.TrackRepository
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.player.impl.AudioPlayerInteractorImpl
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.history.impl.HistoryInteractorImpl
import com.example.playlistmaker.domain.impl.TrackInteractorImpl
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.domain.main_navigation.impl.InternalNavigationInteractorImpl
import com.example.playlistmaker.domain.setting.settings.SettingsInteractor
import com.example.playlistmaker.domain.setting.settings.impl.SettingsInteractorImpl
import com.example.playlistmaker.domain.setting.sharing.SharingInteractor
import com.example.playlistmaker.domain.setting.sharing.impl.SharingInteractorImpl


object Creator {


    private fun getTrackRepository(context: Context): TrackRepository {
        return TrackRepositoryImpl(RetrofitNetworkClient(context))
    }

    fun provideTrackInteractor(context: Context): TrackInteractor {
        return TrackInteractorImpl(getTrackRepository(context))
    }

    fun provideAudioPlayerInteractor(): AudioPlayerInteractor {
        return AudioPlayerInteractorImpl(
            MediaPlayerRepositoryImpl()
        )
    }


    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(
            ExternalNavigationImpl(context)
        )
    }

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(
            SettingsRepositoryImpl(context)
        )
    }

    fun provideHistoryInteractor(context: Context): HistoryInteractor {
        return HistoryInteractorImpl(
            HistoryRepositoryImpl(context)
        )
    }


    fun provideNavigationInteractor(context: Context): InternalNavigationInteractor {
        return InternalNavigationInteractorImpl( InternalNavigationRepositoryImpl(context))
    }


//    fun provideTrackSearchPresenter(context:Context, adapter: TrackAdapter): TrackSearchPresenter {
//        return TrackSearchPresenter(context = context, adapter=adapter)
//    }

//    fun getRepository(context: Context) :SettingsRepositoryImpl {
//        return SettingsRepositoryImpl(context)
//    }
//
//    fun provideSettingInteractor(context:Context): SettingsInteractor {
//        return SettingsInteractorImpl(getRepository(context))
//    }





}
