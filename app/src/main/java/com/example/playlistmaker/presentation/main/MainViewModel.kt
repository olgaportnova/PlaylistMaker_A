package com.example.playlistmaker.presentation.main

import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor

class MainViewModel(private val internalNavigationInteractor: InternalNavigationInteractor) : ViewModel() {

//class MainViewModel(application: Application) : AndroidViewModel(application) {

  //   private val internalNavigationInteractor = Creator.provideNavigationInteractor(getApplication<Application>())


     fun toSettingsScreen() {
          internalNavigationInteractor.toSettingsScreen()
     }

     fun toLibraryScreen() {
          internalNavigationInteractor.toLibraryScreen()
     }

     fun toSearchScreen() {
          internalNavigationInteractor.toSearchScreen()
     }

}