package com.example.playlistmaker.presentation.library


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.ActivityLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryActivity : AppCompatActivity() {
    private val fragList = listOf(
        FavTracksFragment.newInstance(),
        FavPlaylistFragment.newInstance()
    )

    private lateinit var fragListTitles: List<String>
    private lateinit var binding: ActivityLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {

        fragListTitles = listOf(
            getString(R.string.fav_tracks),
            getString(R.string.fav_playlists)
        )

        super.onCreate(savedInstanceState)
        binding = ActivityLibraryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val adapter = ViewPageLibraryAdapter(this, fragList)
        binding.viewPagerLibrary.adapter = adapter

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPagerLibrary) { tab, pos ->
            tab.text = fragListTitles[pos]
        }
        tabMediator.attach()



        binding.back.setOnClickListener {
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }
}



