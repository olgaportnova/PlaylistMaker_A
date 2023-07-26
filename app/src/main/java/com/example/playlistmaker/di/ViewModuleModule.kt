package com.example.playlistmaker.di

import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerViewModel
import com.example.playlistmaker.presentation.library.FavPlaylistFragmentViewModel
import com.example.playlistmaker.presentation.library.FavTracksFragmentViewModel

import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.settings.SettingViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module


    val viewModelModule = module {

//        viewModel {
//            MainViewModel(get())
//        }

        viewModel {
            AudioPlayerViewModel(get())
        }

        viewModel {
            SettingViewModel(get(),get())
        }

        viewModel {
            SearchViewModel(get(),get(),get())
        }

        viewModel {
            FavPlaylistFragmentViewModel()
        }

        viewModel {
            FavTracksFragmentViewModel()
        }






    }
