package com.example.playlistmaker.presentation.tracks


import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import com.example.playlistmaker.R
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.domain.api.TrackInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.TracksState


class TrackSearchPresenter(private val context: Context,
                           private val adapter: TrackAdapter
)
{

    private var view: TracksView? = null
    private var state: TracksState? = null
    fun attachView(view: TracksView) {
        this.view = view
        state?.let {view.render(it)}
    }

    fun detachView() {
        this.view = null
    }

    private val trackInteractor = Creator.provideTrackInteractor(context)

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY = 2000L }


    private val tracks = ArrayList<Track>()

    private val handler = Handler(Looper.getMainLooper())
    private var lastSearchText: String? = null

    private val searchRunnable = Runnable {
        val newSearchText = lastSearchText ?: ""
        searchAction(newSearchText) }

    fun onCreate() {

        adapter.tracks = tracks

    }

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
            renderState(
                TracksState(
                    tracks,
                    true,
                    null,
                    null,
                    false))

            trackInteractor.search(newSearchText, object : TrackInteractor.TrackConsumer {
                override fun consume(foundTracks: List<Track>?,errorMessage:String?) {
                    handler.post {

                        if (foundTracks !=null) {
                            tracks.clear()
                            tracks.addAll(foundTracks)
                        }
                        when {
                            errorMessage!=null -> {
                                renderState(
                                    TracksState(
                                        emptyList(),
                                        false,
                                        context.getString(R.string.something_went_wrong),
                                        R.drawable.something_wrong,
                                    true)
                                    )
                            }
                            tracks.isEmpty() -> {
                                renderState(
                                    TracksState(
                                        emptyList(),
                                        false,
                                        context.getString(R.string.nothing_found),
                                        R.drawable.nothing_found,
                                        false)
                                        )
                            }

                            else -> {
                                renderState(
                                    TracksState(
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
    }

    private fun renderState(state: TracksState) {
        this.state = state
        this.view?.render(state)
    }
}