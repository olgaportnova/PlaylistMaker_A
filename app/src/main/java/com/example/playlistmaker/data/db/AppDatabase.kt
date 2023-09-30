package com.example.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.playlistmaker.data.playlists.dao.PlaylistDao
import com.example.playlistmaker.data.favorites.dao.TrackDao
import com.example.playlistmaker.data.playlists.dao.TrackInPlaylistDao
import com.example.playlistmaker.data.playlists.entity.PlaylistEntity
import com.example.playlistmaker.data.favorites.entity.TrackEntity
import com.example.playlistmaker.data.playlists.entity.TrackInPlaylistsEntity

@Database(version = 5, entities = [TrackEntity::class, PlaylistEntity::class, TrackInPlaylistsEntity::class])
abstract class AppDatabase : RoomDatabase() {

    abstract fun trackDao(): TrackDao

    abstract fun playlistDao(): PlaylistDao

    abstract fun trackInPlaylistDao(): TrackInPlaylistDao


    companion object {

        val MIGRATION_4_5 = object : Migration(4, 5) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "CREATE TABLE playlists (" +
                            "id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                            "name TEXT NOT NULL," +
                            "details TEXT," +
                            "image_path TEXT," +
                            "id_of_tracks TEXT," +
                            "number_of_tracks INTEGER)"
                )
                database.execSQL(
                    "INSERT INTO playlists (id, name, details, image_path, id_of_tracks, number_of_tracks) " +
                            "SELECT id, name, details, image_path, id_of_tracks, number_of_tracks FROM favourite_playlists"
                )

                database.execSQL("DROP TABLE favourite_playlists")
            }
        }



    }
}
