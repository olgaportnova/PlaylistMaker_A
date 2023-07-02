package com.example.playlistmaker.ui.tracks.models

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import com.example.playlistmaker.App
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.util.MyApplication
import androidx.lifecycle.viewmodel.CreationExtras

class TrackViewModel(
    private val trackId: Int,
    private val tracksInteractor: TrackInteractor
    ):ViewModel() {

    private var loadingLiveData = MutableLiveData(true)

    init {
        tracksInteractor.loadSomeData(
            onComplete = {
                loadingLiveData.postValue(false)
                loadingLiveData.value = false
            }
        )
    }

    fun getLoadingLiveData(): LiveData<Boolean> = loadingLiveData

    private var loadingObserver: ((Boolean) -> Unit)? = null

    var isLoading: Boolean = true
        private set(value) {
            field = value
            loadingObserver?.invoke(value)
        }

    fun addLoadingObserver(loadingObserver: ((Boolean) -> Unit)) {
        this.loadingObserver = loadingObserver
    }

    fun removeLoadingObserver() {
        this.loadingObserver = null
    }









    companion object {
        fun getViewModelFactory(trackId:Int,context: Context) : ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create
                            (modelClass: Class<T>,
                            extras: CreationExtras,
                ): T {
                    val application = checkNotNull(extras[APPLICATION_KEY])

                    return TrackViewModel(
                        trackId,
                        (application as MyApplication).provideTracksInteractor()
                    ) as T
                }
            }
    }
}