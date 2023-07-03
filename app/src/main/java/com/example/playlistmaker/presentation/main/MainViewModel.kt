package com.example.playlistmaker.presentation.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor


class MainViewModel(
     private val internalNavigationInteractor: InternalNavigationInteractor
): ViewModel() {


     fun toSettingsScreen() {
          internalNavigationInteractor.toSettingsScreen()
     }

     fun toLibraryScreen() {
          internalNavigationInteractor.toLibraryScreen()
     }

     fun toSearchScreen() {
          internalNavigationInteractor.toSearchScreen()
     }


     companion object {


          fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
               initializer {
                    val interactor =
                         (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App).provideNavigationInteractor(context)
                    MainViewModel(
                         interactor
                    )
               }
          }
     }
}