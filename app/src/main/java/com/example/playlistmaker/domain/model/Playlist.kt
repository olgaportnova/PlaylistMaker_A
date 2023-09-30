package com.example.playlistmaker.domain.model

import java.io.Serializable

data class Playlist(
    val id: Int,
    val name: String,
    val details: String?,
    val imagePath: String?,
    var idOfTracks: List<Int>?,
    val numberOfTracks: Int? =  idOfTracks?.size ?:0

): Serializable
