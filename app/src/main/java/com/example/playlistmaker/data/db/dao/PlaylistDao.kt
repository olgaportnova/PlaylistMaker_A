package com.example.playlistmaker.data.db.dao

import com.example.playlistmaker.data.db.entity.PlaylistEntity
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface PlaylistDao {

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun createPlaylist(playlist: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM favourite_playlists")
    suspend fun getAllPlaylists(): List<PlaylistEntity>

    @Update
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Query("""
    UPDATE favourite_playlists 
    SET 
        id_of_tracks = CASE 
                           WHEN id_of_tracks IS NULL OR id_of_tracks = '' 
                           THEN :trackId 
                           ELSE id_of_tracks || ',' || :trackId 
                       END,
        number_of_tracks = CASE 
                               WHEN number_of_tracks IS NULL 
                               THEN 1 
                               ELSE number_of_tracks + 1 
                           END
    WHERE id = :playlistId
""")
    suspend fun addTrackToPlaylist(playlistId: Int, trackId: String)


}

