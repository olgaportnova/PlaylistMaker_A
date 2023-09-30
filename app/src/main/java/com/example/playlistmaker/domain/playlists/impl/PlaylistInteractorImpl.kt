package com.example.playlistmaker.domain.playlists.impl

import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.domain.playlists.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playlistRepository: PlaylistRepository
): PlaylistInteractor {


    override fun getTracksOnlyFromPlaylist(listOfId:List<Int>): Flow<List<Track>?> {
        return playlistRepository.getTracksOnlyFromPlaylist(listOfId)
    }

    override fun getAllFavouritePlaylists(): Flow<List<Playlist>> {
        return playlistRepository.getAllFavouritePlaylists()
    }

    override  suspend fun getPlaylistsById(id:Int): Playlist {
        return playlistRepository.getPlaylistsById(id)
    }

    override suspend fun createNewPlaylist(playlist: Playlist) {
        playlistRepository.createNewPlaylist(playlist)
    }

    override suspend fun deleteNewPlaylist(playlist: Playlist) {
        playlistRepository.deleteNewPlaylist(playlist)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playlistRepository.updatePlaylist(playlist)
    }

    override suspend fun addTrackToPlaylist(playlistId:Int, trackId:String) {
        playlistRepository.addTrackToPlaylist(playlistId, trackId)
    }

    override suspend fun insertTrackDetailIntoPlaylistInfo(track: Track) {
        playlistRepository.insertTrackDetailIntoPlaylistInfo(track)
    }

    override suspend fun deleteTrackFromPlaylist(updatedListOfTracks: List<Int>?, playlistId:Int, idTrackToDelete:Int) {
        playlistRepository.deleteTrackFromPlayList(updatedListOfTracks, playlistId, idTrackToDelete)
    }

    override suspend fun updateDbListOfTracksInAllPlaylists(id: Int, idTrackToDelete: Int) {
        playlistRepository.updateDbListOfTracksInAllPlaylists(id, idTrackToDelete)
    }

}