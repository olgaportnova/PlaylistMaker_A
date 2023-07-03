package com.example.playlistmaker.presentation.audioPlayer

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import java.util.*

class AudioPlayerViewModel (private val audioPlayerInterator: AudioPlayerInteractor)
    : ViewModel() {



    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = createUpdateTimerTask()


    private var statePlayerLiveData = MutableLiveData(State.PREPARED)

    fun getStatePlayerLiveData(): LiveData<State> = statePlayerLiveData

    private var currentTimerLiveData = MutableLiveData(0)

    fun getCurrentTimerLiveData(): LiveData<Int> = currentTimerLiveData



    fun preparePlayer(url: String) {
        audioPlayerInterator.preparePlayer(url) { state ->
            when (state) {
                State.PREPARED, State.DEFAULT-> {
                    statePlayerLiveData.postValue(State.PREPARED)
                    mainThreadHandler.removeCallbacks(timerRunnable)
                }
                else -> {}
            }
        }
    }

     fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                var a = audioPlayerInterator.currentPosition()
                mainThreadHandler.postDelayed(this, 300)
                currentTimerLiveData.postValue(a)
            }

        }
    }


        fun changePlayerState () {
                audioPlayerInterator.switchPlayer { state ->
                    when (state) {
                        State.PLAYING -> {
                            mainThreadHandler.removeCallbacks(timerRunnable)
                            mainThreadHandler.post(timerRunnable)
                            statePlayerLiveData.postValue(State.PLAYING)
                        }
                        State.PAUSED -> {
                            mainThreadHandler.removeCallbacks(timerRunnable)
                            statePlayerLiveData.postValue(State.PAUSED)
                        }
                        State.PREPARED-> {
                            mainThreadHandler.removeCallbacks(timerRunnable)
                            mainThreadHandler.post(timerRunnable)
                            statePlayerLiveData.postValue(State.PREPARED)
                        }

                        else -> {}
                    }
                }

        }

     fun onPause() {
         mainThreadHandler.removeCallbacks(timerRunnable)
        audioPlayerInterator.pausePlayer()

    }

    fun onDestroy() {
        mainThreadHandler.removeCallbacks(timerRunnable)

    }

    companion object {

        fun getViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val interactor =
                    (this[APPLICATION_KEY] as App).provideAudioPlayerInteractor()

                AudioPlayerViewModel(
                    interactor
                )
            }
        }
    }
}
