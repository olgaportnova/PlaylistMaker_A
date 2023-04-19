package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.ArrayList

val itunesBaseUrl = "https://itunes.apple.com"
val retrofit = Retrofit.Builder()
    .baseUrl(itunesBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val itunesService = retrofit.create(Itunes::class.java)
var songs = ArrayList<Song>()

data class Song (
    val trackName:String,
    val artistName:String,
    val trackTimeMillis: Int,
    val artworkUrl100:String,
        )

class SongsResponse (
    val results: List<Song>)

interface Itunes {
    @GET ("/search?entity=song")
    fun search(@Query("term") text: String) : Call<SongsResponse>
}