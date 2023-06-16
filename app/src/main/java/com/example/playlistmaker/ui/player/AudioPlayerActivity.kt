package com.example.playlistmaker.ui.player


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.models.Track
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityAudioPlayerBinding

    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT

    private var mainThreadHandler = Handler(Looper.getMainLooper())
    private val timerRunnable = createUpdateTimerTask()


    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val track = intent.getSerializableExtra(TRACK_TO_OPEN) as Track
        var url = track.previewUrl // url превью 30 сек.
        preparePlayer(url)

        // нажание на кнопку Play / Pause
        binding.playButton.setOnClickListener {
            playbackControl()
        }

        // нажание на кнопку назад
        binding.backFromAP.setOnClickListener {
            finish()
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


    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        mainThreadHandler.removeCallbacks(timerRunnable)
        mediaPlayer.stop()
        mediaPlayer.release()
    }

    // взять год трека из полной даты
    private fun getFormattedYear(track: Track): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTime(format.parse(track.releaseDate))
        return calendar.get(Calendar.YEAR).toString()
    }

    // подготовка плеера к воспроизведению превью и окончание проигрывания превью
    private fun preparePlayer(url:String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            binding.currentTime.text = START_TIMER
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
            mainThreadHandler.removeCallbacks(timerRunnable)
            binding.playButton.setImageResource(R.drawable.ic_play_button)
            binding.currentTime.text = START_TIMER
        }
    }

    // начало воспроизведения превью трека
    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        mainThreadHandler.post(
            timerRunnable
        )


    }

    // пауза проигрывания превью
    private fun pausePlayer() {
        mediaPlayer.pause()
        mainThreadHandler.removeCallbacks(timerRunnable)
        binding.playButton.setImageResource(R.drawable.ic_play_button)
        playerState = STATE_PAUSED
    }

    // обновление таймера проигрывания
    private fun createUpdateTimerTask(): Runnable {
        return object:Runnable {
            override fun run() {
                binding.currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format((mediaPlayer.currentPosition))
                mainThreadHandler.postDelayed(this, DELAY_MS)}
            }


    }

    private fun playbackControl() {
        when(playerState) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    companion object {

        const val TRACK_TO_OPEN = "item"


        private const val STATE_DEFAULT = 0
        private const val STATE_PREPARED = 1
        private const val STATE_PLAYING = 2
        private const val STATE_PAUSED = 3


        private const val DELAY_MS = 300L


        private const val START_TIMER = "00:00"
    }

}







