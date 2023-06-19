package com.example.playlistmaker.data.dto

import com.example.playlistmaker.domain.model.Track


class TrackSearchResponse (
    val results: List<Track>) : Response()