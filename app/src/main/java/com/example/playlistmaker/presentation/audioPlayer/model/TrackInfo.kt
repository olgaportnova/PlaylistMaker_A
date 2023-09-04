package com.example.playlistmaker.presentation.audioPlayer.model

import com.example.playlistmaker.domain.model.Track

class TrackInfo (
    val trackName: String,
    val artistName: String,
    val trackTime: String,
    val artworkUrl100: String,
    val trackId:Int,
    val collectionName:String,
    val releaseDate: String,
    val primaryGenreName:String,
    val country:String,
    val previewUrl: String,
    val isFavorite: Boolean
)

