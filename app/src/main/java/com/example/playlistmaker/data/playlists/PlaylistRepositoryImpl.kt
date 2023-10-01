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

    override fun getTracksOnlyFromPlaylist(listOfId: List<Int>): Flow<List<Track>?> = flow {
        val tracksInAllPlaylists = appDatabase.trackInPlaylistDao().getAllTracksFromPlaylists()

        if (tracksInAllPlaylists == null) {
            emit(null)
        } else {
            val filteredTracks = tracksInAllPlaylists.filter { track ->
                listOfId.contains(track.id)
            }
            if (filteredTracks == null) {
                emit(null)
            } else {
                emit(convertFromTrackInPlaylistEntityToTrack(filteredTracks))
            }
        }
    }

    override fun getAllFavouritePlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDatabase.playlistDao().getAllPlaylists()
        emit(convertFromPlaylistEntity(playlists))
    }

    override suspend fun getPlaylistsById(id: Int): Playlist {
        val playlists = appDatabase.playlistDao().getPlaylistsById(id)
        return convertFromPlaylistEntityOnePlaylist(playlists)
    }

    override suspend fun createNewPlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToEntity(playlist)
        appDatabase.playlistDao().createPlaylist(playlistEntity)
    }

    override suspend fun deleteNewPlaylist(playlistId: Int) {
        appDatabase.playlistDao().deletePlaylistById(playlistId)
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        val playlistEntity = convertFromPlaylistToEntity(playlist)
        appDatabase.playlistDao().updatePlaylist(playlistEntity)
    }

    override suspend fun addTrackToPlaylist(playlistId: Int, trackId: String) {
        appDatabase.playlistDao().addTrackToPlaylist(playlistId, trackId)
    }

    override suspend fun insertTrackDetailIntoPlaylistInfo(track: Track) {
        val trackEntity = convertFromTrackToTrackEntity(track)
        appDatabase.trackInPlaylistDao().insertTrack(trackEntity)
    }

    override suspend fun deleteTrackFromPlayList(
        updatedListOfTracks: List<Int>?,
        playlistId: Int,
        idTrackToDelete: Int
    ) {
        val updatedListOfTrackString = updatedListOfTracks?.joinToString(separator = ",")
        appDatabase.playlistDao().deleteTrackFromPlaylist(updatedListOfTrackString, playlistId)
    }

    override suspend fun updateDbListOfTracksInAllPlaylists(id: Int, idTrackToDelete: Int) {
        val playlists = appDatabase.playlistDao().getAllPlaylistsExceptOne(id)

        val hasTrack = playlists.any { playlist ->
            val trackIdsAsStrings = playlist.idOfTracks?.split(",")

            val trackIdsAsInts = trackIdsAsStrings?.mapNotNull {
                val trimmed = it.trim()
                if (trimmed.isNotEmpty()) {
                    trimmed.toInt()
                } else {
                    null
                }
            }

            trackIdsAsInts?.contains(idTrackToDelete) ?: false
        }

        if (!hasTrack) {
            appDatabase.trackInPlaylistDao().deleteTrackByIdFromListOfTracksInPlaylists(idTrackToDelete)
        }
    }

    private fun convertFromPlaylistEntity(playlists: List<PlaylistEntity>): List<Playlist> {
        return playlists.map { playlist -> playlistDbConvertor.map(playlist) }
    }

    private fun convertFromPlaylistEntityOnePlaylist(playlist: PlaylistEntity): Playlist {
        return playlistDbConvertor.map(playlist)
    }


    private fun convertFromPlaylistToEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConvertor.map(playlist)
    }

    private fun convertFromTrackToTrackEntity(track: Track): TrackInPlaylistsEntity {
        return trackInPlaylistsEntityDbConvertor.map(track)
    }

    private fun convertFromTrackInPlaylistEntityToTrack(tracks: List<TrackInPlaylistsEntity>): List<Track> {
        return tracks.map { track -> trackInPlaylistsEntityDbConvertor.map(track) }
    }
}
















