package com.example.playlistmaker.data

import com.example.playlistmaker.data.dto.Response
import com.example.playlistmaker.domain.model.Track

interface NetworkClient {

  fun getTracksFromItunes(dto: Any): Response
}