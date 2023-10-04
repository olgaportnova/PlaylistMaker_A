package com.example.playlistmaker.domain.model

import java.io.Serializable

data class Playlist(
    val id: Int,
    var name: String,
    var details: String?,
    var imagePath: String?,
    var idOfTracks: List<Int>?,
    var numberOfTracks: Int? =  idOfTracks?.size ?:0

): Serializable
