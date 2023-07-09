package com.example.playlistmaker.data.main_navigation

import com.example.playlistmaker.domain.model.Track

interface InternalNavigationRepository {

    fun toSettingsScreen()

    fun toLibraryScreen()

    fun toSearchScreen()

    fun openTrack(track: Track)

}