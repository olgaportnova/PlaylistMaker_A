package com.example.playlistmaker.data.db.convertors

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.domain.model.Playlist
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken

class PlaylistDbConvertor {

    fun map(playlist: Playlist) : PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.details,
            playlist.imagePath,
            convertIdOfTrackToString(playlist.idOfTracks),
            playlist.idOfTracks?.size
        )
    }
    fun map(playlist: PlaylistEntity): Playlist {
        val idOfTracksList = if (playlist.idOfTracks != "null" && !playlist.idOfTracks.isNullOrEmpty()) {
            convertStringOfIdTrackToList(playlist.idOfTracks)
        } else {
            emptyList()
        }

        return Playlist(
            playlist.id,
            playlist.name,
            playlist.details,
            playlist.imagePath,
            idOfTracksList,
            playlist.numberOfTracks ?: 0
        )
    }


    private fun convertIdOfTrackToString(listOfIds: List<Int>?): String? {
        return if (listOfIds ==null) {
            null
        } else {
            Gson().toJson(listOfIds)
        }
    }
    private fun convertStringOfIdTrackToList(stringOfIds: String?): List<Int> {
        return stringOfIds?.split(",")?.mapNotNull { it.toIntOrNull() } ?: emptyList()
    }


}