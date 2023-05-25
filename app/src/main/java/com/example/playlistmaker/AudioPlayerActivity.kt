package com.example.playlistmaker


import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val track = intent.getSerializableExtra(TRACK_TO_OPEN) as Track

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

    companion object {

        const val TRACK_TO_OPEN = "item"
    }

}

private fun getFormattedYear(track: Track): String {
    val format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
    val calendar: Calendar = Calendar.getInstance()
    calendar.setTime(format.parse(track.releaseDate))
    return calendar.get(Calendar.YEAR).toString()
}





