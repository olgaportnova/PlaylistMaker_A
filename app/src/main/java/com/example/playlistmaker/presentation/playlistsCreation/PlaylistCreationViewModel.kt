package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.playlists.PlaylistInteractor

class PlaylistCreationViewModel(
    private val context: Context,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {


    private val imageUrlLiveData = MutableLiveData<String>()
    fun getImageUrlLiveData(): LiveData<String> = imageUrlLiveData

    private val _imagePathLiveData = MutableLiveData<String>()
    val imagePathLiveData: LiveData<String> get() = _imagePathLiveData

    fun saveImage(uri: Uri) {
        val picturesDirectoryPath = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)?.absolutePath ?: ""
        val imagePath = playlistInteractor.saveImageFromUri(uri, picturesDirectoryPath)
        _imagePathLiveData.postValue(imagePath)
    }


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






}