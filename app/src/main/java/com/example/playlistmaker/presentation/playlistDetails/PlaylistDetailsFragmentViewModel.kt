package com.example.playlistmaker.presentation.playlistDetails

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.playlists.PlaylistInteractor
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlaylistDetailsFragmentViewModel(
    private val playlistInteractor: PlaylistInteractor,

    ) : ViewModel() {

    private val _playlistDetails = MutableLiveData<Playlist>()
    val playlistDetails: LiveData<Playlist> get() = _playlistDetails

    private val _tracksLiveData = MutableLiveData<TracksInPlaylistState>()
    val tracksLiveData: LiveData<TracksInPlaylistState> get() = _tracksLiveData




    fun getPlaylistById(id: Int) {
        viewModelScope.launch {
            val playlist = playlistInteractor.getPlaylistsById(id)
            _playlistDetails.value = playlist
        }
    }

    fun getTracksFromOnePlaylist(playlist: Playlist) {
        val listOfId = playlist.idOfTracks?.toList()
        viewModelScope.launch {
            playlistInteractor.getTracksOnlyFromPlaylist(listOfId!!)
                .collect { track ->
                    processResult(track)
                }
        }
    }

    private fun getTracksFromOnePlaylistUpdated(updatedListOfId: List<Int>?) {
        if (updatedListOfId?.isEmpty() == true|| updatedListOfId==null) {
            processResult(emptyList())
        } else {
            viewModelScope.launch {
                playlistInteractor.getTracksOnlyFromPlaylist(updatedListOfId!!)
                    .collect { track ->
                        processResult(track)
                    }
            }
        }
    }

    private fun processResult(tracks: List<Track>?) {
        var totalDuration = 0
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
        _tracksLiveData.postValue(state)
    }
    suspend fun deleteTrackFromPlaylist(track: Track, playlist: Playlist) {
       val idTrackToDelete = track.trackId
       var listOfIdsInPlaylist = playlist.idOfTracks?.toMutableList()
       listOfIdsInPlaylist?.remove(idTrackToDelete)
           playlistInteractor.deleteTrackFromPlaylist(listOfIdsInPlaylist?.toList(), playlist.id, idTrackToDelete)
           getTracksFromOnePlaylistUpdated(listOfIdsInPlaylist?.toList())
        viewModelScope.launch(Dispatchers.IO) {
            updateDbListOfTracksInAllPlaylists(playlist.id, idTrackToDelete)
        }

    }

    suspend fun updateDbListOfTracksInAllPlaylists(playlistId:Int, idTrackToDelete: Int) {
        playlistInteractor.updateDbListOfTracksInAllPlaylists(playlistId, idTrackToDelete)
    }


}




