package com.example.playlistmaker.presentation.audioPlayer

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.db.FavouriteInteractor
import com.example.playlistmaker.domain.db.PlaylistInteractor
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.ui.tracks.models.ResultOfAddingState
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch

class AudioPlayerViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val audioPlayerInterator: AudioPlayerInteractor,
    private val favouriteInterator: FavouriteInteractor,
) : ViewModel() {


    private var timerJob: Job? = null

    private var statePlayerLiveData = MutableLiveData(State.PREPARED)
    fun getStatePlayerLiveData(): LiveData<State> = statePlayerLiveData

    private var currentTimerLiveData = MutableLiveData(0)
    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData

    private var isFavourite = MutableLiveData<Boolean>()
    fun getIsFavourite(): LiveData<Boolean> = isFavourite


    private var allFavouritePlaylistsLivaData = MutableLiveData<List<Playlist>>()
    fun getAllFavouritePlaylistsLivaData(): LiveData<List<Playlist>> =
        allFavouritePlaylistsLivaData


    private var statusOfAddingTrackInPlaylistLiveData = MutableLiveData<ResultOfAddingState>()
    fun getStatusOfAddingTrackInPlaylistLiveData(): LiveData<ResultOfAddingState> =
        statusOfAddingTrackInPlaylistLiveData


    fun preparePlayer(url: String) {
        audioPlayerInterator.preparePlayer(url) { state ->
            when (state) {
                State.PREPARED, State.DEFAULT -> {
                    statePlayerLiveData.postValue(State.PREPARED)
                    timerJob?.cancel()
                    currentTimerLiveData.postValue(TIMER_START)

                }

                else -> Unit
            }
        }
    }


    suspend fun getListOfPlaylist(): List<Playlist> {
        return playlistInteractor.getAllFavouritePlaylists().last()
    }


    suspend fun checkIfTrackIsFavorite(trackId: Int): Boolean {
        val favIndicatorsDeferred = viewModelScope.async {
            favouriteInterator.getIdOfFavouriteTracks()
        }
        val favIndicators: Flow<List<Int>> = favouriteInterator.getIdOfFavouriteTracks()

        val favIndicatorsList: MutableList<Int> = mutableListOf()

        favIndicators.collect { list ->
            favIndicatorsList.addAll(list)
        }

        return favIndicatorsList.contains(trackId)
    }


    private fun startTimer(state: State) {
        timerJob = viewModelScope.launch {
            while (state == State.PLAYING) {
                delay(DELAY_UPDATE_TIMER_MC)
                currentTimerLiveData.postValue(audioPlayerInterator.currentPosition())
            }
        }
        if (state == State.PREPARED) {
            timerJob?.cancel()
            currentTimerLiveData.postValue(TIMER_START)
        }
    }

    suspend fun onFavoriteClicked(track: Track) {
        if (track.isFavorite) {
            audioPlayerInterator.deleteTrackFromFav(track)
            isFavourite.postValue(false)
            track.isFavorite = false

        } else {
            audioPlayerInterator.addTrackToFav(track)
            isFavourite.postValue(true)
            track.isFavorite = true
        }
    }


    fun changePlayerState() {
        audioPlayerInterator.switchPlayer { state ->
            when (state) {
                State.PLAYING -> {
                    startTimer(State.PLAYING)
                    currentTimerLiveData.postValue(audioPlayerInterator.currentPosition())
                    statePlayerLiveData.postValue(State.PLAYING)

                }

                State.PAUSED -> {
                    statePlayerLiveData.postValue(State.PAUSED)
                    timerJob?.cancel()

                }

                State.PREPARED -> {
                    timerJob?.cancel()
                    startTimer(State.PREPARED)
                    statePlayerLiveData.postValue(State.PREPARED)
                    currentTimerLiveData.postValue(TIMER_START)

                }

                State.DEFAULT -> {
                    timerJob?.cancel()
                    statePlayerLiveData.postValue(State.DEFAULT)
                }
            }
        }
    }

    fun onPause() {
        statePlayerLiveData.postValue(State.PAUSED)
        audioPlayerInterator.pausePlayer()

    }


    fun onResume() {
        statePlayerLiveData.postValue(State.PAUSED)

    }

    suspend fun addTrackToPlaylist(playlist: Playlist, track: Track) {
        if (playlist.idOfTracks?.contains(track.trackId) == true) {
            statusOfAddingTrackInPlaylistLiveData.postValue(
                ResultOfAddingState.ALREADY_EXISTS(playlist)
            )
        } else {
            viewModelScope.launch {
                playlistInteractor.addTrackToPlaylist(playlist.id, track.trackId.toString())
                playlistInteractor.insertTrackDetailIntoPlaylistInfo(track)
                statusOfAddingTrackInPlaylistLiveData.postValue(ResultOfAddingState.SUCCESS(playlist))


                val updatedPlaylists = playlistInteractor.getAllFavouritePlaylists().last()
                allFavouritePlaylistsLivaData.postValue(updatedPlaylists)
            }
        }
    }


    companion object {
        const val TIMER_START = 0
        const val DELAY_UPDATE_TIMER_MC = 300L

    }
}



