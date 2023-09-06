package com.example.playlistmaker.di

import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.library.playlists.FavPlaylistFragmentViewModel
import com.example.playlistmaker.presentation.library.tracks.FavTracksFragmentViewModel
import com.example.playlistmaker.presentation.playlistsCreation.PlaylistCreationViewModel

import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


    val viewModelModule = module {


        viewModel {
            AudioPlayerViewModel(get(),get(),get())
        }

        viewModel { (activity: AppCompatActivity) ->
            PlaylistCreationViewModel(androidContext(), get(), activity)
        }

        viewModel {
            SettingViewModel(get(),get())
        }

        viewModel {
            SearchViewModel(get(),get(),get())
        }

        viewModel {
            FavPlaylistFragmentViewModel(androidContext(), get())
        }

        viewModel {
            FavTracksFragmentViewModel(androidContext(), get())
        }




    }
