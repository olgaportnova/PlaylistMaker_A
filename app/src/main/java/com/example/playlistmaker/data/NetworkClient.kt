package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response

interface NetworkClient {

  suspend fun getTracksFromItunes(dto: Any): Response
}