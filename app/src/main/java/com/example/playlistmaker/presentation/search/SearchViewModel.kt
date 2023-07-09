package com.example.playlistmaker.presentation.search


import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState
import com.example.playlistmaker.util.Creator


class SearchViewModel(
    application: Application,
) : AndroidViewModel(application), TrackAdapter.Listener {

    private val searchInteractor = Creator.provideTrackInteractor(getApplication<Application>())
    private val historyInteractor = Creator.provideHistoryInteractor(getApplication<Application>())
    private val internalNavigationInteractor =
        Creator.provideNavigationInteractor(getApplication<Application>())


    private var searchTrackStatusLiveData = MutableLiveData<TracksState>()

    fun getSearchTrackStatusLiveData(): LiveData<TracksState> = searchTrackStatusLiveData


    private var tracks = ArrayList<Track>()
    private val adapter = TrackAdapter(tracks, this)
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText
        if (newSearchText!!.isEmpty()) {
            getHistory()
        } else {
            searchAction(newSearchText)
        }
    }

    fun onCreate() {
        adapter.tracks = tracks
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    fun onResume() {
        getHistory()
        if (tracks.isEmpty()){
            showHistory()
        }
    }

    // поиск по вводу каждые 2 сек
    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun getHistory():ArrayList<Track> {
        return  historyInteractor.getHistoryList()
    }
    fun showHistory() {
        searchTrackStatusLiveData.postValue(
            TracksState(
                emptyList(),
                false,
                null,
                needToUpdate = false,
                toShowHistory = true,
                history = getHistory(),
            )
        )
    }

    fun clearHistory() {
        historyInteractor.clearHistory()
    }

    fun addNewTrackToHistory(track: Track) {
        historyInteractor.addTrackToHistory(track)
    }

    fun openTrackAudioPlayer(track: Track) {
        internalNavigationInteractor.openTrack(track)
    }

    fun searchAction(newSearchText: String) {

        if (newSearchText.isNotEmpty()) {
            searchTrackStatusLiveData.postValue(
                TracksState(
                    tracks,
                    true,
                    null,
                    needToUpdate = false,
                    toShowHistory = false,
                    history = emptyList(),
                )
            )
        }

        searchInteractor.search(newSearchText, object : TrackInteractor.TrackConsumer {
            override fun consume(foundTracks: List<Track>?, errorMessage: String?) {
                handler.post {

                    if (foundTracks != null) {
                        tracks.clear()
                        tracks.addAll(foundTracks)
                    }
                    when {
                        errorMessage != null -> {
                            searchTrackStatusLiveData.postValue(
                                TracksState(
                                    emptyList(),
                                    false,
                                    ERROR_CONNECTION,
                                    needToUpdate = true,
                                    toShowHistory = false,
                                    history = emptyList(),
                                )
                            )
                        }
                        tracks.isEmpty() -> {
                            searchTrackStatusLiveData.postValue(
                                TracksState(
                                    emptyList(),
                                    false,
                                    ERROR_EMPTY_LIST,
                                    needToUpdate = false,
                                    toShowHistory = false,
                                    history = emptyList(),
                                )
                            )
                        }

                        else -> {
                            searchTrackStatusLiveData.postValue(
                                TracksState(
                                    tracks,
                                    false,
                                    null,
                                    needToUpdate = false,
                                    toShowHistory = false,
                                    history = emptyList(),
                                )
                            )
                        }
                    }
                }
            }
        })
    }
    override fun onClick(track: Track) {
        addNewTrackToHistory(track)
        getHistory()
        openTrackAudioPlayer(track)
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val ERROR_CONNECTION = -1
        private const val ERROR_EMPTY_LIST = -2

        fun getViewModelFactory(): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    SearchViewModel(this[APPLICATION_KEY] as Application)
                }
            }
    }

}


