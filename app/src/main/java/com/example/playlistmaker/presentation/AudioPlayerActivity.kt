package com.example.playlistmaker


import android.annotation.SuppressLint
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding
    private var mediaPlayer = MediaPlayer()
    private var playerState = STATE_DEFAULT
    private var mainThreadHandler: Handler? = null

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainThreadHandler = Handler(Looper.getMainLooper())

        val track = intent.getSerializableExtra(TRACK_TO_OPEN) as Track
        var url = track.previewUrl
        preparePlayer(url)

        binding.playButton.setOnClickListener {
            playbackControl()
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
        val buttonBack = findViewById<View>(R.id.back_from_AP)
        buttonBack.setOnClickListener {
            finish()
        }

    }
    override fun onPause() {
        super.onPause()
        pausePlayer()
    }


    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer.stop()

    }

    private fun getFormattedYear(track: Track): String {
        val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val calendar: Calendar = Calendar.getInstance()
        calendar.setTime(format.parse(track.releaseDate))
        return calendar.get(Calendar.YEAR).toString()
    }

    private fun preparePlayer(url:String) {
        binding.currentTime.text = "00:00"
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            binding.currentTime.text = "00:00"
        }
        mediaPlayer.setOnCompletionListener {
            mediaPlayer.stop()
            playerState = STATE_PREPARED
            binding.playButton.setImageResource(R.drawable.ic_play_button)
            binding.currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format((mediaPlayer.currentPosition))
        }
    }

    private fun startPlayer() {
        mediaPlayer.start()
        binding.playButton.setImageResource(R.drawable.ic_pause)
        playerState = STATE_PLAYING
        binding.currentTime.text = "00:00"
        mainThreadHandler?.post(
            createUpdateTimerTask()
        )


    }
    private fun createUpdateTimerTask(): Runnable {
        return object:Runnable {
            override fun run() {
                if (mediaPlayer.currentPosition < PREVIEW_DURATION) {
                    binding.currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format((mediaPlayer.currentPosition))
                    mainThreadHandler?.postDelayed(this, DELAY)}
                else {
                    binding.currentTime.text = "00:00"

                }


            }

        }
    }

    private fun pausePlayer() {
        mediaPlayer.pause()
        mainThreadHandler?.removeCallbacks(createUpdateTimerTask())
        binding.playButton.setImageResource(R.drawable.ic_play_button)
        playerState = STATE_PAUSED
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


        private const val DELAY = 300L

        private const val PREVIEW_DURATION = 30000L
    }

}







