package com.example.playlistmaker.domain.model

import com.example.playlistmaker.presentation.audioPlayer.model.TrackInfo
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Track(
    val trackName: String,
    val artistName: String,
    val trackTimeMillis: Int,
    val artworkUrl100: String,
    val trackId:Int,
    val collectionName:String,
    val releaseDate: String,
    val primaryGenreName:String,
    val country:String,
    val previewUrl: String

): Serializable {



fun toTrackInfo(track:Track) = TrackInfo(
    trackName = track.trackName,
    artistName = track.artistName,
    trackTime = SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis),
    artworkUrl100 = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"),
    trackId = track.trackId,
    collectionName = track.collectionName,
    releaseDate = track.getFormattedYear(track),
    primaryGenreName = track.primaryGenreName,
    country = track.country,
    previewUrl = track.previewUrl

)

private fun getFormattedYear(track: Track): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTime(format.parse(track.releaseDate))
    return calendar.get(Calendar.YEAR).toString()
}

}
