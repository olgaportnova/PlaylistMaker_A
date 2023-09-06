package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.data.db.dao.PlaylistDao
import com.example.playlistmaker.data.db.dao.TrackDao
import com.example.playlistmaker.data.db.dao.TrackInPlaylistDao
import com.example.playlistmaker.data.db.entity.PlaylistEntity
import com.example.playlistmaker.data.db.entity.TrackEntity
import com.example.playlistmaker.data.db.entity.TrackInPlaylistsEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistsEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackInPlaylistDao(): TrackInPlaylistDao


    companion object {

        val MIGRATION_3_4: Migration = object : Migration(3, 4) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
            CREATE TABLE IF NOT EXISTS tracks_in_playlists (
                track_id INTEGER PRIMARY KEY,
                artworkUrl100 TEXT NOT NULL,
                trackName TEXT NOT NULL,
                artistName TEXT NOT NULL,
                collectionName TEXT NOT NULL,
                releaseDate TEXT NOT NULL,
                primaryGenreName TEXT NOT NULL,
                country TEXT NOT NULL,
                trackTimeMillis INTEGER NOT NULL,
                previewUrl TEXT NOT NULL,
                timestamp INTEGER DEFAULT (strftime('%s','now'))
            )
        """
                )
            }
        }

    }
}
