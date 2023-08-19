package com.example.playlistmaker.presentation.audioPlayer

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AudioPlayerViewModel(private val audioPlayerInterator: AudioPlayerInteractor ) : ViewModel() {


    private var timerJob: Job? = null

    private var statePlayerLiveData = MutableLiveData(State.PREPARED)

    fun getStatePlayerLiveData(): LiveData<State> = statePlayerLiveData

    private var currentTimerLiveData = MutableLiveData(0)

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData


    fun preparePlayer(url: String) {
        audioPlayerInterator.preparePlayer(url) { state ->
            when (state) {
                State.PREPARED, State.DEFAULT -> {
                    statePlayerLiveData.postValue(State.PREPARED)
                }
                else -> Unit
            }
        }
    }


    private fun startTimer(state: State) {
        timerJob = viewModelScope.launch {
            while (state==State.PLAYING) {
                delay(DELAY_UPDATE_TIMER_MC)
                currentTimerLiveData.postValue(audioPlayerInterator.currentPosition())
            }
            timerJob?.cancel()
        }
    }

    fun changePlayerState() {
        audioPlayerInterator.switchPlayer { state ->
            when (state) {
                State.PLAYING -> {
                    currentTimerLiveData.postValue(audioPlayerInterator.currentPosition())
                    statePlayerLiveData.postValue(State.PLAYING)
                    startTimer(state)
                }
                State.PAUSED -> {
                    timerJob?.cancel()
                    statePlayerLiveData.postValue(State.PAUSED)
                }
                State.PREPARED -> {
                    startTimer(state)
                    statePlayerLiveData.postValue(State.PREPARED)
                    currentTimerLiveData.postValue(TIMER_START)
                }
                State.DEFAULT -> {
                    timerJob?.cancel()
                    statePlayerLiveData.postValue(State.DEFAULT)
                    currentTimerLiveData.postValue(TIMER_START)
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

    companion object {
        const val TIMER_START = 0
        const val DELAY_UPDATE_TIMER_MC = 300L

    }
}
