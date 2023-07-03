package com.example.playlistmaker

import com.example.playlistmaker.domain.model.Track


data class SongsResponse (
    val results: List<Track>)