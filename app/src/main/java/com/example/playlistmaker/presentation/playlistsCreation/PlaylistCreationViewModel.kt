package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.os.Environment
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

    private var urlImageForNewPlaylist : String? = null


    suspend fun createNewPlaylist(
        name: String,
        details: String,
        tracksId: List<Int>?,
        numberOfTracks: Int?
    ) {
        val newPlaylist = Playlist(0, name, details, urlImageForNewPlaylist, tracksId, numberOfTracks)
        playlistInteractor.createNewPlaylist(newPlaylist)
    }

    fun getImageUrlFromStorage(playlistName: String) {
        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
        val url = file.toUri().toString()
        imageUrlLiveData.postValue(url)
        urlImageForNewPlaylist = url
    }



    fun renameImageFile(playlistName: String) {
        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
        val temporaryFileName = "cover.jpg"
        val temporaryFile = File(filePath, temporaryFileName)

        if (temporaryFile.exists()) {
            val finalFile = File(filePath, "cover_$playlistName.jpg")
            temporaryFile.renameTo(finalFile)
        }
    }
}