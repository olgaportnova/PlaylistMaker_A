package com.example.playlistmaker.presentation.playlistDetails

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentPlaylistDetailsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.search.SearchFragment
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.ui.tracks.models.PlaylistsState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsFragment : Fragment(), TrackAdapter.Listener {

    private val viewModel: PlaylistDetailsFragmentViewModel by activityViewModel()
    private val searchTrackViewModel: SearchViewModel by viewModel()
    private var isClickAllowed = true

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int? = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaylistDetailsBinding.inflate(inflater, container, false)
        playlistId = arguments?.getInt("playlistId")
        viewModel.getPlaylistById(playlistId!!)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObservers()
        setupBackButton()
    }

    private fun setupUI() {
        binding.recycleViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setupObservers() {
        viewModel.getPlaylistDetails().observe(viewLifecycleOwner, ::initUi)
        viewModel.getTracksLiveData().observe(viewLifecycleOwner, ::handleTracksState)
    }

    private fun setupBackButton() {
        binding.icBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun handleTracksState(state: TracksInPlaylistState) {
        when (state) {
            is TracksInPlaylistState.Content -> showContent(state.tracks, state.trackDurations)
            is TracksInPlaylistState.Empty -> showEmpty()
            is TracksInPlaylistState.Loading -> {}
            else -> {}
        }
    }

    private fun showEmpty() {
        with(binding) {
            recycleViewBottomSheet.visibility = View.GONE
            playlistMinutes.text = "0 минут"
            playlistTracks.text = "0 треков"
        }
    }

    private fun showContent(tracks: List<Track>, duration: Int) {
        val formattedDuration = getFormattedDuration(duration)
        with(binding) {
            recycleViewBottomSheet.adapter = TrackAdapter(ArrayList(tracks), this@PlaylistDetailsFragment)
            recycleViewBottomSheet.visibility = View.VISIBLE
            playlistMinutes.text = "$formattedDuration ${getMinuteWordForm(formattedDuration.toInt() ?: 0)}"
        }
    }

    private fun getFormattedDuration(duration: Int): Int {
        return SimpleDateFormat("mm", Locale.getDefault()).format(duration).toInt()
    }

    private fun initUi(playlist: Playlist) {
        binding.recycleViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())

        if (playlist.idOfTracks?.isNotEmpty() == true) {
            viewModel.getTracksFromOnePlaylist(playlist.idOfTracks!!.toList())
        } else {
            showEmpty()
        }

        with(binding) {
            playlistName.text = playlist.name
            playlistDetails.text = playlist.details
            playlistTracks.text = "${playlist.numberOfTracks.toString()} ${getTrackWordForm(playlist.numberOfTracks ?: 0)}"
            Glide.with(root.context)
                .load(playlist.imagePath)
                .placeholder(R.drawable.placeholder_big)
                .into(imagePlaylistCover)
        }

        if (playlist.details?.isEmpty() == true) {
            binding.playlistDetails.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun getTrackWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }

    fun getMinuteWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "минут"
            count % 10 == 1 -> "минута"
            count % 10 in 2..4 -> "минуты"
            else -> "минут"
        }
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchTrackViewModel.openTrackAudioPlayer(track)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            viewLifecycleOwner.lifecycleScope.launch {
                delay(SearchFragment.CLICK_DEBOUNCE_DELAY_MC)
                isClickAllowed = true
            }
        }
        return current
    }
}
