package com.example.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.playlistmaker.databinding.FragmentFavTracksBinding
import com.example.playlistmaker.databinding.FragmentSearchBinding
import com.example.playlistmaker.domain.model.Track
import com.example.playlistmaker.presentation.search.SearchFragment
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.example.playlistmaker.ui.tracks.models.FavoriteState
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment(), TrackAdapter.Listener {

    private val searchTrackViewModel: SearchViewModel by viewModel()
    private val favTracksFragmentViewModel: FavTracksFragmentViewModel by viewModel()
    private lateinit var binding: FragmentFavTracksBinding
    private var isClickAllowed = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rcTrackList.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        favTracksFragmentViewModel.fillData()
        favTracksFragmentViewModel.observeState().observe(viewLifecycleOwner) {
            render(it)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.rcTrackList.adapter = null
    }

    override fun onResume() {
        super.onResume()
        favTracksFragmentViewModel.fillData()
    }

    private fun render(state: FavoriteState) {
        when (state) {
            is FavoriteState.Content -> showContent(state.tracks)
            is FavoriteState.Empty -> showEmpty(state.message)
            is FavoriteState.Loading -> {}
        }
    }

    private fun showEmpty(message: String) {
        binding.rcTrackList.visibility = View.GONE
        binding.imageNoFavTracks.visibility = View.VISIBLE
        binding.textPlaceholder.visibility = View.VISIBLE

        binding.textPlaceholder.text = message
    }

    private fun showContent(tracks: List<Track>) {
        binding.rcTrackList.visibility = View.VISIBLE
        binding.imageNoFavTracks.visibility = View.GONE
        binding.textPlaceholder.visibility = View.GONE

        binding.rcTrackList.adapter = TrackAdapter(ArrayList(tracks), this@FavTracksFragment)

    }


    companion object {
        @JvmStatic
        fun newInstance() = FavTracksFragment()
    }

    override fun onClick(track: Track) {
        if (clickDebounce()) {
            searchTrackViewModel.openTrackAudioPlayer(track)
        }
    }

    // контроль нажатий на трек (не быстрее чем 1 сек)
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