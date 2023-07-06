package com.example.playlistmaker.presentation.search


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.playlistmaker.App
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState



class SearchViewModel(
    private val searchInteractor: TrackInteractor
) : ViewModel() {

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

    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }


    fun onDestroy() {
        handler.removeCallbacks(searchRunnable)
    }

    // поиск по вводу каждые 2 сек
    fun searchDebounce(changedText: String) {
        this.lastSearchText = changedText
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }


    fun searchAction(newSearchText: String) {
        if (newSearchText.isNotEmpty()) {
            searchTrackStatusLiveData.postValue(TracksState(
                    tracks,
                    true,
                    null,
                    null,
                    false
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
                                searchTrackStatusLiveData.postValue(TracksState(
                                        emptyList(),
                                        false,
                                    "Проблемы со связью \n \n Загрузка не удалась. Проверьте подключение к интернету",
                                      //  Resources.getSystem().getString(R.string.something_went_wrong),
                                        R.drawable.something_wrong,
                                        true
                                    )
                                )
                            }
                            tracks.isEmpty() -> {
                                searchTrackStatusLiveData.postValue(TracksState(
                                        emptyList(),
                                        false,
                                    "Ничего не нашлось",
                                     //   Resources.getSystem().getString(R.string.nothing_found),
                                        R.drawable.nothing_found,
                                        false
                                    )
                                )
                            }

                            else -> {
                                searchTrackStatusLiveData.postValue(TracksState(
                                        tracks,
                                        false,
                                        null,
                                        null,
                                        false
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
                    SearchViewModel(
                        searchInteractor,
                    )
                }
            }
    }

    }


