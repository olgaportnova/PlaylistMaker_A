package com.example.playlistmaker.presentation.main

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.util.Creator


class MainViewModel(application: Application) : AndroidViewModel(application) {

     private val internalNavigationInteractor = Creator.provideNavigationInteractor(getApplication<Application>())


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
                    MainViewModel(this[APPLICATION_KEY] as Application)
               }
          }
     }
}