package com.example.playlistmaker.domain.main_navigation.impl

import com.example.playlistmaker.data.main_navigation.InternalNavigationRepository
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor

class InternalNavigationInteractorImpl (private val internalNavigationRepository: InternalNavigationRepository) : InternalNavigationInteractor {
    override fun toSettingsScreen() {
        internalNavigationRepository.toSettingsScreen()
    }

    override fun toLibraryScreen() {
        internalNavigationRepository.toLibraryScreen()
    }

    override fun toSearchScreen() {
        internalNavigationRepository.toSearchScreen()
    }
}