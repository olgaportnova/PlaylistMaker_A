package com.example.playlistmaker


import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.google.gson.Gson
import java.text.SimpleDateFormat
import java.util.*

class AudioPlayerActivity() : AppCompatActivity() {
    private lateinit var binding: ActivityAudioPlayerBinding

    @SuppressLint("SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val item1 = intent.getStringExtra("item")
        if (item1 != null) {
            var item = createTrackFromJson(item1)
            binding.apply {
                trackName.text = item.trackName
                artistName.text = item.artistName
                durationResult.text =
                    SimpleDateFormat("mm:ss", Locale.getDefault()).format(item.trackTimeMillis)
                Glide.with(coverAP).load(item.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg"))
                    .placeholder(R.drawable.placeholder_big).into(coverAP)
                countryResult.text = item.country
                yearResult.text = item.releaseDate.subSequence(0, 4)
                if (item.collectionName != null) {
                    albumResult.text = item.collectionName
                } else {
                    binding.album.visibility = View.GONE
                    binding.albumResult.visibility = View.GONE
                }
                genreResult.text = item.primaryGenreName


            }
        }


        val buttonBack = findViewById<View>(R.id.back_from_AP)
        buttonBack.setOnClickListener {
            finish()
    }








    }
    private fun createTrackFromJson(json: String): Track {
        return Gson().fromJson(json, Track::class.java)
    }
}



