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
import java.util.UUID

class PlaylistCreationViewModel(
    private val context: Context,
    private val playlistInteractor: PlaylistInteractor,
) : ViewModel() {


    private val imageUrlLiveData = MutableLiveData<String>()
    fun getImageUrlLiveData(): LiveData<String> = imageUrlLiveData

    private var urlImageForNewPlaylist: String? = null
    private var temporaryFile: File? = null

    private var finalUrl: String? = null


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

//    fun getImageUrlFromStorage(playlistName: String) {
//        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
//        val url = file.toUri().toString()
//        imageUrlLiveData.postValue(url)
//        urlImageForNewPlaylist = url
//    }
//    fun getImageUrlFromStorageEdit(playlistName: String): String {
//        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
//        val url = file.toUri().toString()
//        imageUrlLiveData.postValue(url)
//        return url
//    }

//    fun renameImageFile() {
//        val filePath = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), "myalbum")
//        val temporaryFileName = "12345.jpg"
//        temporaryFile = File(filePath, temporaryFileName)
//
//        // Генерация уникального идентификатора для имени файла
//        val uniqueID = UUID.randomUUID().toString()
//        val finalFile = File(filePath, "cover_$uniqueID.jpg")
//        finalUrl = finalFile.absolutePath
//
//        if (finalFile.exists()) {
//            finalFile.delete()
//        }
//
//        if (temporaryFile!!.exists()) {
//            temporaryFile!!.renameTo(finalFile)
//            if (!finalFile.exists()) {
//                urlImageForNewPlaylist = finalFile.toString()
//            }
//        }
//    }





}