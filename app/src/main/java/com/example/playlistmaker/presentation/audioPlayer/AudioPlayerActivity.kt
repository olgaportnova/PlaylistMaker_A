package com.example.playlistmaker.presentation.audioPlayer

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.data.history.impl.TRACK_TO_OPEN
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.audioPlayer.model.TrackInfo
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity (): AppCompatActivity() {

    private val viewModel: AudioPlayerViewModel by viewModel()

    private lateinit var binding: ActivityAudioPlayerBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(TRACK_TO_OPEN) as Track
        val trackInfo = track.toTrackInfo(track)
        val url = track.previewUrl // url превью 30 сек.


        viewModel.getStatePlayerLiveData().observe(this) { state ->
            changeState(state)
        }

        viewModel.getCurrentTimerLiveData().observe(this) { currentTimer ->
            changeTimer(currentTimer)
        }

        viewModel.preparePlayer(url)


        binding.playButton.setOnClickListener {
            viewModel.changePlayerState()
        }

        // нажание на кнопку назад
        binding.backFromAP.setOnClickListener {
            finish()
        }

        init(trackInfo)

    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()

    }

    override fun onResume() {
        viewModel.onResume()
        super.onResume()

    }

    override fun onDestroy() {
        super.onDestroy()

    }

    private fun changeState(state: State) {
        if (state == State.PAUSED) {
            binding.playButton.setImageResource(R.drawable.ic_play_button)
        }
        if (state == State.PLAYING) {
            binding.playButton.setImageResource(R.drawable.ic_pause)
        }
        if (state == State.PREPARED) {
            binding.playButton.setImageResource(R.drawable.ic_play_button)
            binding.currentTime.text = "00:00"
        }
        if (state == State.DEFAULT) {
            binding.playButton.setImageResource(R.drawable.ic_play_button)
            binding.currentTime.text = "00:00"
        }

    }

    private fun changeTimer(currentTimer: Int) {
        binding.currentTime.text =
            SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimer)
    }


    private fun init(trackInfo: TrackInfo) {
        binding.apply {
            binding.currentTime.text = "00:00"
            trackName.text = trackInfo.trackName
            artistName.text = trackInfo.artistName
            durationResult.text = trackInfo.trackTime
            Glide.with(coverAP)
                .load(trackInfo.artworkUrl100).placeholder(R.drawable.placeholder_big).into(coverAP)
            countryResult.text = trackInfo.country
            yearResult.text = trackInfo.releaseDate
            genreResult.text = trackInfo.primaryGenreName
            if (trackInfo.collectionName != null) {
                albumResult.text = trackInfo.collectionName
            } else {
                binding.album.visibility = View.GONE
                binding.albumResult.visibility = View.GONE
            }
        }
    }

}


