package com.example.playlistmaker

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.io.Serializable
import java.util.ArrayList


val itunesBaseUrl = "http://itunes.apple.com"
val retrofit = Retrofit.Builder()
    .baseUrl(itunesBaseUrl)
    .addConverterFactory(GsonConverterFactory.create())
    .build()

val itunesService = retrofit.create(Itunes::class.java)

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId: Int,
    val collectionName: String?,
    val releaseDate: String,
    val primaryGenreName: String,
    val country: String,
    val previewUrl: String
) : Serializable


data class SongsResponse(
    val results: List<Track>
)

interface Itunes {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SongsResponse>
}