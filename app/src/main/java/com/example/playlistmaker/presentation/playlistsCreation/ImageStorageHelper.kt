package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.os.Environment
import android.util.Log
import java.io.File

object ImageStorageHelper {

    fun getImageFileDirectory(context: Context): File {
        return File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
    }

    fun getTemporaryImageFile(context: Context): File {
        val directory = getImageFileDirectory(context)
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val tempFile = File(directory, "12345.jpg")
        return tempFile
    }


        fun getImageFileForPlaylist(context: Context, playlistName: String): File {
            return File(getImageFileDirectory(context), "cover_$playlistName.jpg")
        }
    }

