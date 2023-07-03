package com.example.playlistmaker

import com.example.playlistmaker.data.network.Itunes
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





