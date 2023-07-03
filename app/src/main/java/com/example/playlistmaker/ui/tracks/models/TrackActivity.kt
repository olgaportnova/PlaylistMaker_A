package com.example.playlistmaker.ui.tracks.models

import android.os.Bundle
import android.os.PersistableBundle
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModelProvider

class TrackActivity : ComponentActivity() {

    private lateinit var viewModel: TrackViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(
            this,
            TrackViewModel.getViewModelFactory(123, this)
        )[TrackViewModel::class.java]


        viewModel.getLoadingLiveData().observe(this) { isLoading ->
            changeProgressBarVisibility(isLoading)
        }

//        changeProgressBarVisibility(viewModel.isLoading)
//        viewModel.addLoadingObserver { isLoading ->
//            changeProgressBarVisibility(isLoading)
//        }


    }

//    override fun onDestroy() {
//        super.onDestroy()
//        viewModel.removeLoadingObserver()
//    }

    private fun changeProgressBarVisibility(visible: Boolean) {
        // Обновляем видимость прогресс-бара
    }
}