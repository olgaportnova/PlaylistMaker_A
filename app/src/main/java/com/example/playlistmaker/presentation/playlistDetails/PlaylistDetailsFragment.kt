package com.example.playlistmaker.presentation.playlistDetails

import android.content.DialogInterface
import android.graphics.Color
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
import com.example.playlistmaker.presentation.search.SearchViewModel
import com.example.playlistmaker.presentation.search.TrackAdapter
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.activityViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.text.SimpleDateFormat
import java.util.Locale

class PlaylistDetailsFragment : Fragment(), TrackAdapter.OnItemClickListener, TrackAdapter.OnItemLongClickListener {

    private val viewModel: PlaylistDetailsFragmentViewModel by activityViewModel()
    private val searchTrackViewModel: SearchViewModel by viewModel()

    private var _binding: FragmentPlaylistDetailsBinding? = null
    private val binding get() = _binding!!

    private var playlistId: Int? = -1
    private lateinit var playlistToCompareId: Playlist

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
        viewModel.playlistDetails.observe(viewLifecycleOwner, ::initUi)
        viewModel.tracksLiveData.observe(viewLifecycleOwner, ::handleTracksState)
    }
    private fun setupBackButton() {
        binding.icBackArrow.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun initUi(playlist: Playlist) {

        playlistToCompareId = playlist
        binding.recycleViewBottomSheet.layoutManager = LinearLayoutManager(requireContext())

        viewModel.getTracksFromOnePlaylist(playlist)

        with(binding) {
            playlistName.text = playlist.name
            playlistDetails.text = playlist.details
            playlistTracks.text =
                "${playlist.numberOfTracks.toString()} ${getTrackWordForm(playlist.numberOfTracks ?: 0)}"
            Glide.with(root.context)
                .load(playlist.imagePath)
                .placeholder(R.drawable.placeholder_big)
                .into(imagePlaylistCover)
        }

        if (playlist.details?.isEmpty() == true) {
            binding.playlistDetails.visibility = View.GONE
        }
    }

    private fun showContent(tracks: List<Track>, duration: Int) {
        val formattedDuration = getFormattedDuration(duration)
        val numberOfTracks = tracks.size
        with(binding) {
            recycleViewBottomSheet.adapter = TrackAdapter(
                ArrayList(tracks),
                this@PlaylistDetailsFragment,
                this@PlaylistDetailsFragment
            )
            recycleViewBottomSheet.visibility = View.VISIBLE
            playlistMinutes.text =
                "$formattedDuration ${getMinuteWordForm(formattedDuration.toInt() ?: 0)}"
            playlistTracks.text =
                "${numberOfTracks.toString()} ${getTrackWordForm(numberOfTracks ?: 0)}"
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



    override fun onItemClick(track: Track) {
            searchTrackViewModel.openTrackAudioPlayer(track)

    }

    override fun onItemLongClick(track: Track): Boolean {
        MaterialAlertDialogBuilder(requireContext())
            .setTitle(context?.getString(R.string.dialog_delete_title))
            .setMessage(context?.getString(R.string.dialog_delete_message))
            .setNeutralButton(context?.getString(R.string.dialog_delete_cancel)) { dialog, _ ->
                dialog.dismiss()
            }
            .setPositiveButton(getString(R.string.dialog_delete_delete)) { _, _ ->
                lifecycleScope.launch {
                    viewModel.deleteTrackFromPlaylist(track, playlistToCompareId)
                }
            }
            .setOnDismissListener {
            }
            .show()
            .apply {
                getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(Color.parseColor("#3772E7"))
                getButton(DialogInterface.BUTTON_NEUTRAL).setTextColor(Color.parseColor("#3772E7"))
            }

        return true
    }



    private fun getTrackWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }
    private fun getMinuteWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "минут"
            count % 10 == 1 -> "минута"
            count % 10 in 2..4 -> "минуты"
            else -> "минут"
        }
    }
    private fun getFormattedDuration(duration: Int): Int {
        return SimpleDateFormat("mm", Locale.getDefault()).format(duration).toInt()
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


}
