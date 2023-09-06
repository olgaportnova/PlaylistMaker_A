package com.example.playlistmaker.presentation.search


import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.domain.api_search.TrackInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.main_navigation.InternalNavigationInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume


class SearchViewModel(
    private val searchInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor,
    private val internalNavigationInteractor: InternalNavigationInteractor
) : ViewModel(){
    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val ERROR_CONNECTION = -1
        private const val ERROR_EMPTY_LIST = -2

    }


    private var searchTrackStatusLiveData = MutableLiveData<TracksState>()

    fun getSearchTrackStatusLiveData(): LiveData<TracksState> = searchTrackStatusLiveData


    private var tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null
    private var searchJob: Job? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText
        if (newSearchText!!.isEmpty()) {
            getHistory()
            showHistory()
        } else {
            searchAction(newSearchText)
        }
    }

    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
        lastSearchText=null
    }

    fun onResume() {

    }


    // поиск по вводу каждые 2 сек

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText) {
            return
        }
        if (changedText.isEmpty()) {
            showHistory()
            return
        }

        lastSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY)
            searchAction(changedText)

        }
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
        viewModelScope.launch {
            searchInteractor
                .search(newSearchText)
                .collect { pair ->
                    processResult(pair.first, pair.second)
                }
        }
    }

    suspend fun updateIdOfFavTracks(): List<Int> {
        return suspendCancellableCoroutine { continuation ->
            viewModelScope.launch {
                searchInteractor
                    .getFavIndicators()
                    .collect { favIndicators ->
                        continuation.resume(favIndicators)
                    }
            }
        }
    }

    private fun processResult(foundTracks: List<Track>?, errorMessage: String?) {
        val tracks = mutableListOf<Track>()
        if (foundTracks != null) {
            tracks.clear()
            tracks.addAll(foundTracks)
        }
        when { errorMessage != null -> {
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