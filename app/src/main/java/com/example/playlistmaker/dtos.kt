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
var tracks = ArrayList<Track>()
var trackHistory = ArrayList<Track>()

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId:Int
)

class SongsResponse (
    val results: List<Track>)

interface Itunes {
    @GET ("/search?entity=song")
    fun search(@Query("term") text: String) : Call<SongsResponse>
}