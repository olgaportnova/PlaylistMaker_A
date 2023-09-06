package com.example.playlistmaker.domain.playlists

import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistRepository {

    fun getAllFavouritePlaylists(): Flow<List<Playlist>>
    suspend fun createNewPlaylist(playlist: Playlist)
    suspend fun deleteNewPlaylist(playlist: Playlist)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId:Int, trackId:String)
    suspend fun insertTrackDetailIntoPlaylistInfo(track: Track)
}