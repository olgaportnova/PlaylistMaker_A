

package com.example.playlistmaker.presentation


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.presentation.audioPlayer.AudioPlayerViewModel
import com.example.playlistmaker.util.Creator
import java.text.SimpleDateFormat
import java.util.*

private lateinit var viewModel: AudioPlayerViewModel
class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioPlayerBinding


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra("item") as Track
        val trackInfo = track.toTrackInfo(track)
        val url = track.previewUrl // url превью 30 сек.




        viewModel = ViewModelProvider(
            this,
            AudioPlayerViewModel.getViewModelFactory()
        )[AudioPlayerViewModel::class.java]

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
            super.finish()
        }


        binding.apply {
            trackName.text = trackInfo.trackName
            artistName.text = trackInfo.artistName
            durationResult.text =trackInfo.trackTime
            Glide.with(coverAP).load(trackInfo.artworkUrl100).placeholder(R.drawable.placeholder_big).into(coverAP)
            countryResult.text = trackInfo.country
            yearResult.text = trackInfo.releaseDate
            genreResult.text = trackInfo.primaryGenreName
            if (track.collectionName != null) {
                albumResult.text = track.collectionName }
            else {
                binding.album.visibility = View.GONE
                binding.albumResult.visibility = View.GONE
            }
        }
        binding.currentTime.text = "00:00"
    }

    override fun onPause() {
        viewModel.onPause()
        super.onPause()

    }

    override fun onDestroy() {
        viewModel.onDestroy()
        super.onDestroy()

    }

    private fun changeState(state: State) {
        if (state == State.PAUSED ) {
                binding.playButton.setImageResource(R.drawable.ic_play_button)
            }
        if (state == State.PLAYING)  {
                binding.playButton.setImageResource(R.drawable.ic_pause)
            }
        if (state == State.PREPARED) {
                binding.playButton.setImageResource(R.drawable.ic_play_button)
                binding.currentTime.text ="00:00"
            }
        }

    private fun changeTimer(currentTimer:Int) {
        binding.currentTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimer)
        }
    }
