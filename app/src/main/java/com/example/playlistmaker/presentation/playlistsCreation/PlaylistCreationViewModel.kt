package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.os.Environment
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import java.io.File

class PlaylistCreationViewModel(
    private val context: Context,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {


    private val imageUrlLiveData = MutableLiveData<String>()
    fun getImageUrlLiveData(): LiveData<String> = imageUrlLiveData

    private var urlImageForNewPlaylist: String? = null
    private var temporaryFile: File? = null


    suspend fun createNewPlaylist(
        name: String,
        details: String,
        urlImageForNewPlaylist: String?,
        tracksId: List<Int>?,
        numberOfTracks: Int?
    ) {
        val newPlaylist =
            Playlist(0, name, details, urlImageForNewPlaylist, tracksId, numberOfTracks)
        playlistInteractor.createNewPlaylist(newPlaylist)
    }

    suspend fun editPlaylist(
       playlist: Playlist
    ) {
        playlistInteractor.createNewPlaylist(playlist)
    }

    fun getImageUrlFromStorage(playlistName: String) {
        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
        val url = file.toUri().toString()
        imageUrlLiveData.postValue(url)
        urlImageForNewPlaylist = url
    }
    fun getImageUrlFromStorageEdit(playlistName: String): String {
        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
        val url = file.toUri().toString()
        return url
    }

    fun renameImageFile(playlistName: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val temporaryFileName = "12345.jpg"
        temporaryFile = File(filePath, temporaryFileName)

        if (temporaryFile!!.exists()) {
            Log.d("DEBUG", "Temporary file exists at ${temporaryFile!!.absolutePath}")
        } else {
            Log.e("DEBUG", "Temporary file does NOT exist at ${temporaryFile!!.absolutePath}")
        }

        val finalFile = File(filePath, "cover_$playlistName.jpg")

        if (finalFile.exists()) {
            finalFile.delete()
            if (!finalFile.exists()) {
                Log.d("DEBUG", "Final file was deleted successfully.")
            } else {
                Log.e("DEBUG", "Failed to delete the final file.")
            }
        }

        val finalFile1 = File(filePath, "cover_$playlistName.jpg")

        if (temporaryFile!!.exists()) {
            temporaryFile!!.renameTo(finalFile1)
            if (finalFile.exists()) {
                Log.d("DEBUG", "File was renamed successfully to ${finalFile1.absolutePath}")
            } else {
                Log.e("DEBUG", "Failed to rename the file.")
            }
        } else {
            urlImageForNewPlaylist = null
        }
    }




}