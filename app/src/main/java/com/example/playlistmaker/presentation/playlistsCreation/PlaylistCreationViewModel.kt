package com.example.playlistmaker.presentation.playlistsCreation

import android.content.Context
import android.os.Environment
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerActivity
import com.example.playlistmaker.presentation.main.RootActivity
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import java.io.File

class PlaylistCreationViewModel(
    private val context: Context,
    private val playlistInteractor: PlaylistInteractor,
    private val activity: AppCompatActivity
) : ViewModel() {

    suspend fun createNewPlaylist(
        name: String,
        details: String,
        imageUrl: String,
        tracksId: List<Int>?,
        numberOfTracks: Int?
    ) {
        val newPlaylist = Playlist(
            0,
            name,
            details,
            imageUrl,
            tracksId,
            numberOfTracks
        )
        playlistInteractor.createNewPlaylist(newPlaylist)
    }

    fun getImageUrlFromStorage(playlistName: String): String {
        val file = ImageStorageHelper.getImageFileForPlaylist(context, playlistName)
        return file.toUri().toString()
    }

    fun showBackConfirmationDialog(needToShowDialog: Boolean) {
        if (needToShowDialog) {
            MaterialAlertDialogBuilder(activity)
                .setTitle("Завершить создание плейлиста?")
                .setMessage("Все несохраненные данные будут потеряны")
                .setNeutralButton("Отмена") { dialog, _ ->
                    dialog.dismiss()
                }
                .setPositiveButton("Завершить") { dialog, which ->
                    viewModelScope.launch {
                        navigatingBack(activity = activity)
                    }
                }
                .show()
        } else {
            viewModelScope.launch {
                navigatingBack(activity = activity)
            }
        }
    }

    suspend fun navigatingBack(activity: AppCompatActivity) {
        when (activity) {
            is RootActivity -> {
                activity.onBackPressed()
            }
            is AudioPlayerActivity -> {
                (activity as AudioPlayerActivity).methodToCallFromFragment()
            }
            else -> {}
        }
    }

    fun renameImageFile(playlistName: String) {
        val filePath = File(
            context.getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "myalbum"
        )
        val temporaryFileName = "cover.jpg" // Временное имя файла
        val temporaryFile = File(filePath, temporaryFileName)

        if (temporaryFile.exists()) {
            val finalFile = File(filePath, "cover_$playlistName.jpg") // Окончательное имя файла
            temporaryFile.renameTo(finalFile) // Переименовать файл
        }
    }
}
