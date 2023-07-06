package com.example.playlistmaker.presentation.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track


class HistoryViewModel(
    private val historyInteractor: HistoryInteractor,

) : ViewModel() {

    fun startHistory(): Array<Track> {
        return historyInteractor.getHistoryList()
    }

    private var historyLiveData = MutableLiveData(startHistory())

    fun getHistoryLiveData(): LiveData<Array<Track>> = historyLiveData

    // достаем историю из SP
    fun getUpdatedHistory(): Array<Track> {
        var updatedHistory = historyInteractor.getHistoryList()
        return updatedHistory
        historyLiveData.postValue(updatedHistory)
    }

    fun addNewTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun openTrackAudioPlayer(track: Track) {
        historyInteractor.openTrack(track)
    }


    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val historyInteractor =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App).provideHistoryInteractor(
                        context
                    )
                HistoryViewModel(
                    historyInteractor
                )
            }
        }
    }

}