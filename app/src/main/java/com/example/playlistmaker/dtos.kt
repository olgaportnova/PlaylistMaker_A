package com.example.playlistmaker

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


data class Song (
    val trackName:String,
    val artistName:String,
    val trackTimeMillis: Int,
    val artworkUrl100:String,
        )

class SongsResponse (
    val searchType:String,
    val expression :String,
    val results: List<Song>)

interface Itunes {
    @GET ("/search?entity=song")
    fun search(@Query("term") text: String) : Call<SongsResponse>
}