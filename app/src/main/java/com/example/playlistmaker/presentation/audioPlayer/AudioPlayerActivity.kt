package com.example.playlistmaker.presentation.audioPlayer

import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.data.history.impl.TRACK_TO_OPEN
import com.example.playlistmaker.databinding.ActivityAudioPlayerBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.State
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.audioPlayer.model.TrackInfo
import com.example.playlistmaker.presentation.playlistsCreation.BackNavigationListenerAudioPlayer
import com.example.playlistmaker.presentation.playlistsCreation.PlaylistCreationFragment
import com.example.playlistmaker.ui.tracks.models.ResultOfAddingState
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AudioPlayerActivity (
) : AppCompatActivity(), BackNavigationListenerAudioPlayer {

    private val viewModel: AudioPlayerViewModel by viewModel()
    private lateinit var binding: ActivityAudioPlayerBinding
    private lateinit var track: Track
    private lateinit var adapter: PlaylistsAdapterBottomSheet
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayout>

    // lifecycle functions
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAudioPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        track = intent.getSerializableExtra(TRACK_TO_OPEN) as Track
        val trackInfo = track.toTrackInfo(track)
        val url = track.previewUrl // url превью 30 сек.

        setupViewModelObservers()
        setupUIComponents(trackInfo)
        setupBottomSheetBehaviorCallback()
        viewModel.preparePlayer(url)
    }
    override fun onPause() {
        viewModel.onPause()
        super.onPause()
    }
    override fun onResume() {
        viewModel.onResume()
        super.onResume()
    }



    private fun init(trackInfo: TrackInfo) {
        viewModel.checkIfTrackIsFavorite(trackInfo.trackId)
        val radius = resources.getDimensionPixelSize(R.dimen.album_cover_corner_radius)

        binding.apply {
            currentTime.text = "00:00"
            trackName.text = trackInfo.trackName
            artistName.text = trackInfo.artistName
            durationResult.text = trackInfo.trackTime

            Glide.with(coverAP)
                .load(trackInfo.artworkUrl100)
                .placeholder(R.drawable.placeholder_big)
                .centerCrop()
                .transform(RoundedCorners(radius))
                .into(coverAP)

            countryResult.text = trackInfo.country
            yearResult.text = trackInfo.releaseDate
            genreResult.text = trackInfo.primaryGenreName

            if (trackInfo.collectionName != null) {
                albumResult.text = trackInfo.collectionName
            } else {
                album.visibility = View.GONE
                albumResult.visibility = View.GONE
            }
        }
    }
    private fun setupUIComponents(trackInfo: TrackInfo) {
        lifecycleScope.launch {
            setupBottomSheet()
        }
        setupClickListeners()
        init(trackInfo)
    }
    // затемнение экрана при открытии BottomSheet
    private fun setupBottomSheetBehaviorCallback() {
        val dimOverlay: View = binding.dimOverlay

        bottomSheetBehavior.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                when (newState) {
                    BottomSheetBehavior.STATE_EXPANDED -> {
                        dimOverlay.alpha = 1f
                        dimOverlay.visibility = View.VISIBLE
                    }
                    else -> {
                        dimOverlay.visibility = View.GONE
                    }
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                dimOverlay.alpha = slideOffset
            }
        })
    }
    private suspend fun setupBottomSheet() {
        val bottomSheetContainer = binding.standardBottomSheet
        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheetContainer)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        binding.recycleViewBottomSheet.layoutManager = LinearLayoutManager(this)

        adapter = PlaylistsAdapterBottomSheet(this, mutableListOf(), object : PlaylistsAdapterBottomSheet.Listener {
            override fun onClick(playlist: Playlist) {
                lifecycleScope.launch {
                    viewModel.addTrackToPlaylist(playlist, track)
                }
            }
        })

        binding.recycleViewBottomSheet.adapter = adapter

    }
    private fun setupClickListeners() {
        binding.playButton.setOnClickListener {
            viewModel.changePlayerState()
        }

        binding.favoriteButton.setOnClickListener {
            lifecycleScope.launch {
                viewModel.onFavoriteClicked(track = track)
            }
        }

        binding.backFromAP.setOnClickListener {
            finish()
        }

        binding.addToPLButton.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            binding.dimOverlay.visibility = View.VISIBLE
        }

        binding.buttonCreateNewPlaylist.setOnClickListener {
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            binding.scrollViewMain.visibility=View.GONE
            binding.fragmentContainer.visibility=View.VISIBLE
            val fragmentTransaction = supportFragmentManager.beginTransaction()
            fragmentTransaction.replace(R.id.fragment_container, PlaylistCreationFragment())
            fragmentTransaction.addToBackStack(null)
            fragmentTransaction.commit()
        }
    }
    private fun setupViewModelObservers() {
        viewModel.apply {
            getCurrentTimerLiveData().observe(this@AudioPlayerActivity, ::changeTimer)
            getStatePlayerLiveData().observe(this@AudioPlayerActivity, ::changeState)
            getTrackIsFavoriteLiveData().observe(this@AudioPlayerActivity, ::changeFavouriteIcon)


            getStatusOfAddingTrackInPlaylistLiveData().observe(this@AudioPlayerActivity) { status ->
                showToastOnResultOfAddingTrack(status)
            }

            viewModel.getAllFavouritePlaylistsLivaData().observe(this@AudioPlayerActivity, Observer { playlists ->
                adapter.updateData(playlists)

            })

            getListOfPlaylistsLiveData().observe(this@AudioPlayerActivity, Observer { updatedPlaylists ->
                adapter.updateData(updatedPlaylists)
            })

        }
    }
    private fun showToastOnResultOfAddingTrack(status: ResultOfAddingState?) {
        when (status) {
            is ResultOfAddingState.ALREADY_EXISTS -> {
                Toast.makeText(this, "Трек уже добавлен в плейлист ${status.playlists.name}", Toast.LENGTH_SHORT).show()
            }
            is ResultOfAddingState.SUCCESS -> {
                Toast.makeText(this, "Добавлено в плейлист ${status.playlists.name}", Toast.LENGTH_SHORT).show()
            }
            else -> { }
        }
    }
    private fun changeState(state: State) {
        when (state) {
            State.PAUSED -> binding.playButton.setImageResource(R.drawable.ic_play_button)
            State.PLAYING -> binding.playButton.setImageResource(R.drawable.ic_pause)
            State.PREPARED, State.DEFAULT -> {
                binding.playButton.setImageResource(R.drawable.ic_play_button)
                binding.currentTime.text = "00:00"
            }
        }
    }
    private fun changeTimer(currentTimer: Int) {
        binding.currentTime.text = SimpleDateFormat("mm:ss", Locale.getDefault()).format(currentTimer)
    }
    private fun changeFavouriteIcon(favourite: Boolean?) {
        if (favourite == true) {
            binding.favoriteButton.setImageResource(R.drawable.ic_fav_botton_pressed)
        } else {
            binding.favoriteButton.setImageResource(R.drawable.ic_fav_botton)
        }
    }



    // navigation functions
    fun methodToCallFromFragment() {
        // Удаляем фрагмент из fragmentContainer
        val fragmentManager = supportFragmentManager
        val fragment = fragmentManager.findFragmentById(R.id.fragment_container)

        if (fragment != null) {
            fragmentManager.beginTransaction()
                .remove(fragment)
                .commit()
        }

        binding.fragmentContainer.visibility = View.GONE
        viewModel.getListOfPlaylist()
        binding.scrollViewMain.visibility = View.VISIBLE
    }
    override fun onBackPressed() {
            val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
            if (currentFragment is PlaylistCreationFragment) {
                onNavigateBack(false)
            } else {
                super.onBackPressed()
            }
        }
    private fun backCheckFragment() {
        val currentFragment = supportFragmentManager.findFragmentById(R.id.fragment_container)
        if (currentFragment is PlaylistCreationFragment)  {
                lifecycleScope.launch {
                    currentFragment.navigateBack()
                }
            } else {
                super.onBackPressed()
            }
        }
    override fun onNavigateBack(isEmpty: Boolean) {
        if (isEmpty) methodToCallFromFragment()
        else backCheckFragment()

    }


}
