package com.example.playlistmaker.presentation.library.playlists

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavPlaylistsBinding
import com.example.playlistmaker.domain.model.Playlist
import com.example.playlistmaker.ui.tracks.models.PlaylistsState
import org.koin.androidx.viewmodel.ext.android.activityViewModel

class FavPlaylistFragment : Fragment() {

    private val favPlaylistFragmentViewModel: FavPlaylistFragmentViewModel by activityViewModel()

    private lateinit var adapter: PlaylistsAdapter

    private var _binding: FragmentFavPlaylistsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavPlaylistsBinding.inflate(inflater, container, false)
        adapter = PlaylistsAdapter(requireContext(), mutableListOf(), object : PlaylistsAdapter.Listener {
            override fun onClick(playlist: Playlist) {
                val bundle = Bundle()
                bundle.putSerializable("playlist", playlist)
                findNavController().navigate(R.id.playlistDetailsFragment, bundle)
            }
        })
        binding.recyclerViewPlaylists.adapter = adapter

        binding.buttonCreateNewPlaylist.setOnClickListener {
            navigateToNewFragment()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = GridLayoutManager(context,2)
        binding.recyclerViewPlaylists.layoutManager = layoutManager
     //   binding.recyclerViewPlaylists.addItemDecoration(GridSpacingItemDecoration(2, 8,16))

        favPlaylistFragmentViewModel.observeState().observe(viewLifecycleOwner) { state ->
            when (state) {
                is PlaylistsState.Content -> showContent(state.playlists)
                is PlaylistsState.Empty -> showEmpty()
                is PlaylistsState.Loading -> {}
                else -> {}
            }
        }

        favPlaylistFragmentViewModel.fillData()
    }


    private fun showEmpty() {
        binding.recyclerViewPlaylists.visibility = View.GONE
        binding.imageNoPlaylists.visibility = View.VISIBLE
        binding.textNoPlaylists.visibility = View.VISIBLE
    }


    private fun showContent(playlists: List<Playlist>) {
        binding.recyclerViewPlaylists.visibility = View.VISIBLE
        binding.imageNoPlaylists.visibility = View.GONE
        binding.textNoPlaylists.visibility = View.GONE

        adapter.updateData(playlists)
    }




    private fun navigateToNewFragment() {
        findNavController().navigate(R.id.playlistFragment,null)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = FavPlaylistFragment()
    }
}
