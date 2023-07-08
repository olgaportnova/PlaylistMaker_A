package com.example.playlistmaker.presentation.search


import android.app.Application
import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.history.HistoryInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState
import com.example.playlistmaker.util.Creator


class SearchViewModel(
    application: Application,
    private val searchInteractor: TrackInteractor,
    private val historyInteractor: HistoryInteractor

) : AndroidViewModel(application) {



    private var searchTrackStatusLiveData = MutableLiveData<TracksState>()

    fun getSearchTrackStatusLiveData(): LiveData<TracksState> = searchTrackStatusLiveData


    private val tracks = ArrayList<Track>()
    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchAction(newSearchText)
    }

//    fun onCreate() {
//
//        adapter.tracks = tracks
//
//    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    // поиск по вводу каждые 2 сек
    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    fun getHistory() {
       var updatedHistory =  historyInteractor.getHistoryList()
        searchTrackStatusLiveData.postValue(
            TracksState(
                emptyList(),
                false,
                null,
                needToUpdate = false,
                toShowHistory = true,
                history = updatedHistory,
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
        historyInteractor.openTrack(track)
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
                                    "Проблемы со связью \n \n Загрузка не удалась. Проверьте подключение к интернету",
                                    //  Resources.getSystem().getString(R.string.something_went_wrong),
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
                                    "Ничего не нашлось",
                                    //   getApplication<Application>().getString(R.string.nothing_found),
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

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L

        fun getViewModelFactory(context: Context): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    val searchInteractor =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App).provideTrackInteractor(
                            context
                        )
                    val historyInteractor =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App).provideHistoryInteractor(
                            context
                        )
                    SearchViewModel(
                        Application(),
                        searchInteractor,
                        historyInteractor
                    )
                }
            }
    }

}


