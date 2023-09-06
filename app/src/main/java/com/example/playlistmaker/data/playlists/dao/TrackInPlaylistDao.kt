package com.example.playlistmaker.data.playlists.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.playlistmaker.data.playlists.entity.TrackInPlaylistsEntity

@Dao
interface TrackInPlaylistDao {

    @Insert(entity = TrackInPlaylistsEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTrack(track: TrackInPlaylistsEntity)


    @Query("SELECT * FROM favourite_tracks WHERE track_id = :trackId")
    suspend fun getTrackById(trackId: Int): TrackInPlaylistsEntity?


}