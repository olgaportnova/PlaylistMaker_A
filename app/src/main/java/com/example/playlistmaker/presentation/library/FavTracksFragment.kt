package com.example.playlistmaker.presentation.library

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentFavPlaylistsBinding
import com.example.playlistmaker.databinding.FragmentFavTracksBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class FavTracksFragment : Fragment() {


    private val favTracksFragmentViewModel: FavTracksFragmentViewModel by viewModel()

    private lateinit var binding: FragmentFavTracksBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        binding = FragmentFavTracksBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }

    companion object {
        @JvmStatic
        fun newInstance() = FavTracksFragment()
    }
}