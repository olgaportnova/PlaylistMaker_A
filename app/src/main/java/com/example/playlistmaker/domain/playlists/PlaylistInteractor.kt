package com.example.playlistmaker.domain.playlists

import android.net.Uri
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    fun saveImageFromUri(uri: Uri, picturesDirectoryPath: String): String

    fun getTracksOnlyFromPlaylist(listOfId:List<Int>): Flow<List<Track>?>

    fun getAllFavouritePlaylists(): Flow<List<Playlist>>
    suspend fun getPlaylistsById(id:Int): Playlist
    suspend fun createNewPlaylist(playlist: Playlist)
    suspend fun deleteNewPlaylist(playlistId: Int)
    suspend fun updatePlaylist(playlist: Playlist)
    suspend fun addTrackToPlaylist(playlistId:Int, trackId:String)
    suspend fun insertTrackDetailIntoPlaylistInfo(track: Track)
    suspend fun deleteTrackFromPlaylist(toList: List<Int>?, playlistId:Int, idTrackToDelete:Int)
    suspend fun updateDbListOfTracksInAllPlaylists(id: Int, idTrackToDelete: Int)
}