package com.example.playlistmaker.data.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName="tracks_in_playlists")
data class TrackInPlaylistsEntity(
    @PrimaryKey @ColumnInfo(name = "track_id")
    val id: Int,
    val artworkUrl100: String,
    val trackName: String,
    val artistName: String,
    val collectionName:String,
    val releaseDate: String,
    val primaryGenreName:String,
    val country:String,
    val trackTimeMillis: Int,
    val previewUrl: String,
    val timestamp: Long = System.currentTimeMillis()



)
