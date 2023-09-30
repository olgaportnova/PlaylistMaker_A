package com.example.playlistmaker.data.playlists.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName="playlists")
data class PlaylistEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val name: String,
    @ColumnInfo(name = "details")
    val details: String?,
    @ColumnInfo(name = "image_path")
    val imagePath: String?,
    @ColumnInfo(name = "id_of_tracks")
    val idOfTracks: String?,
    @ColumnInfo(name = "number_of_tracks")
    val numberOfTracks: Int?
)
