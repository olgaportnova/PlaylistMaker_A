package com.example.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavPlaylistsBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavPlaylistFragment : Fragment() {

    private val favPlaylistFragmentViewModel: FavPlaylistFragmentViewModel by viewModel()

    private lateinit var binding: FragmentFavPlaylistsBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavPlaylistsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        }



//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//
//        return inflater.inflate(R.layout.fragment_fav_playlists,container,false)
//    }

    companion object {
        @JvmStatic
        fun newInstance() = FavPlaylistFragment()
    }
}