package com.example.playlistmaker.presentation


import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.util.Creator
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.player.AudioPlayerInteractor
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.*


class AudioPlayerActivity : AppCompatActivity() {


    private lateinit var binding: ActivityAudioPlayerBinding
    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = createUpdateTimerTask()
    private val audioPlayerInteractor: AudioPlayerInteractor =
        Creator.provideAudioPlayerInteractor()

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra("item") as Track
        val url = track.previewUrl // url превью 30 сек.

        preparePlayer(url)
        // нажание на кнопку Play / Pause
        binding.playButton.setOnClickListener {
            audioPlayerInteractor.switchPlayer { state ->
                when (state) {
                    State.PAUSED -> {
                        mainThreadHandler.removeCallbacks(timerRunnable)
                        binding.playButton.setImageResource(R.drawable.ic_play_button)
                    }
                    State.PLAYING, State.PREPARED -> {
                        binding.playButton.setImageResource(R.drawable.ic_pause)
                        mainThreadHandler.removeCallbacks(timerRunnable)
                        mainThreadHandler.post(timerRunnable)
                    }
                    else -> {}
                }
            }
        }

        // нажание на кнопку назад
        binding.backFromAP.setOnClickListener {
            super.finish()
        }

        binding.apply {
            trackName.text = track.trackName
            artistName.text = track.artistName
            durationResult.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            Glide.with(coverAP).load(track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                .placeholder(R.drawable.placeholder_big).into(coverAP)
            countryResult.text = track.country
            yearResult.text = getFormattedYear(track)

            if (track.collectionName != null) {
                albumResult.text = track.collectionName
            } else {
                binding.album.visibility = View.GONE
                binding.albumResult.visibility = View.GONE
            }
            genreResult.text = track.primaryGenreName
        }
        binding.currentTime.text = "00:00"
    }

    override fun onPause() {
        super.onPause()
        audioPlayerInteractor.pausePlayer()

    }

    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(timerRunnable)
    }

    // взять год трека из полной даты
    private fun getFormattedYear(track: Track): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTime(format.parse(track.releaseDate))
        return calendar.get(Calendar.YEAR).toString()
    }

    // подготовка плеера к воспроизведению превью и окончание проигрывания превью
    private fun preparePlayer(url: String) {
        audioPlayerInteractor.preparePlayer(url) { state ->
            when (state) {
                State.PREPARED -> {
                    mainThreadHandler.removeCallbacks(timerRunnable)
                    binding.playButton.setImageResource(R.drawable.ic_play_button)
                    binding.currentTime.text = "00:00"
                }
                else -> {}
            }
        }
    }

    // обновление таймера проигрывания
    private fun createUpdateTimerTask(): Runnable {
        return object : Runnable {
            override fun run() {
                binding.currentTime.text = SimpleDateFormat(
                    "mm:ss",
                    Locale.getDefault()
                ).format((audioPlayerInteractor.currentPosition()))
                mainThreadHandler.postDelayed(this, 300)
            }
        }
    }

}

