package com.example.playlistmaker.presentation.playlistDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import com.example.playlistmaker.ui.tracks.models.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistDetailsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,

    ) : ViewModel() {

    private val playlistDetails = MutableLiveData<Playlist>()
    fun getPlaylistDetails(): LiveData<Playlist> = playlistDetails

    private val tracksLiveData = MutableLiveData<TracksInPlaylistState>()
    fun getTracksLiveData(): LiveData<TracksInPlaylistState> = tracksLiveData



    fun getPlaylistById(id: Int) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistsById(id)
            playlistDetails.value = playlist
        }
    }

    fun getTracksFromOnePlaylist(listOfId: List<Int>) {
        viewModelScope.launch {
            playlistInteractor.getTracksOnlyFromPlaylist(listOfId)
                .collect { track ->
                    processResult(track)

                }
        }
    }

    private fun processResult(tracks: List<Track>?) {
        var totalDuration = 0
        val a = tracks

        if (tracks.isNullOrEmpty()) {
            renderState(TracksInPlaylistState.Empty)
        } else {
            for (track in tracks) {
                totalDuration += track.trackTimeMillis
            }
            renderState(TracksInPlaylistState.Content(tracks, totalDuration))
        }
    }

    private fun renderState(state: TracksInPlaylistState) {
        tracksLiveData.postValue(state)
    }


}




