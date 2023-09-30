package com.example.playlistmaker.data.playlists

import com.example.playlistmaker.data.db.AppDatabase
import com.example.playlistmaker.data.db.convertors.PlaylistDbConvertor
import com.example.playlistmaker.data.db.convertors.TrackInPlaylistsEntityDbConvertor
import com.example.playlistmaker.data.playlists.entity.PlaylistEntity
import com.example.playlistmaker.data.playlists.entity.TrackInPlaylistsEntity
import com.example.playlistmaker.domain.playlists.PlaylistRepository
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlaylistRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val playlistDbConvertor: PlaylistDbConvertor,
    private val trackInPlaylistsEntityDbConvertor: TrackInPlaylistsEntityDbConvertor
) : PlaylistRepository {

    override fun getAllFavouritePlaylists(): Flow<List<Playlist>>  = flow {
       val playlists = appDatabase.playlistDao().getAllPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun createNewPlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToEntity(playlist)
        appDatabase.playlistDao().createPlaylist(playlistEntity)
    }

    override suspend fun deleteNewPlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToEntity(playlist)
        appDatabase.playlistDao().deletePlaylist(playlistEntity)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToEntity(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun addTrackToPlaylist(playlistId:Int, trackId:String) {
        appDatabase.playlistDao().addTrackToPlaylist(playlistId, trackId)
    }

    override suspend fun insertTrackDetailIntoPlaylistInfo(track: Track) {
        val trackEntity = convertFromTrackToTrackEntity(track)
        appDatabase.trackInPlaylistDao().insertTrack(trackEntity)
    }





    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist) }

    private fun convertFromTrackToTrackEntity(track: Track): TrackInPlaylistsEntity {
        return trackInPlaylistsEntityDbConvertor.map(track) }

    }














