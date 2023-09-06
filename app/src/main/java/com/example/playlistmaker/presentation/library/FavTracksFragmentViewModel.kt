package com.example.playlistmaker.presentation.library

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.playlistmaker.R
import com.example.playlistmaker.domain.db.FavouriteInteractor
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.ui.tracks.models.FavoriteState
import com.example.playlistmaker.ui.tracks.models.TracksState
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class FavTracksFragmentViewModel(
    private val context: Context,
    private val favouriteInteractor: FavouriteInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteState>()
    fun observeState(): LiveData<FavoriteState> = stateLiveData

    fun fillData() {
        renderState(FavoriteState.Loading)
        viewModelScope.launch {
            favouriteInteractor.getAllFavouriteTracks()
                .collect { tracks ->
                    processResult(tracks)
                }
        }
    }

    private fun processResult(tracks: List<Track>) {
        if (tracks.isEmpty()) {
            renderState(FavoriteState.Empty(context.getString(R.string.noFavTracksFound)))
        } else {
            for (track in tracks) {
                track.isFavorite = true
            }
            renderState(FavoriteState.Content(tracks))
        }
    }

    private fun renderState(state: FavoriteState) {
        stateLiveData.postValue(state)
    }


}


