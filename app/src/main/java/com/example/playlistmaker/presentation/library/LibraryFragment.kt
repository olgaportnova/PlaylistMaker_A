package com.example.playlistmaker.presentation.library

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.FragmentLibraryBinding
import com.google.android.material.tabs.TabLayoutMediator

class LibraryFragment : Fragment() {

    private val fragList = listOf(
        FavTracksFragment.newInstance(),
        FavPlaylistFragment.newInstance()
    )

    private lateinit var fragListTitles: List<String>
    private lateinit var binding: FragmentLibraryBinding

    private lateinit var tabMediator: TabLayoutMediator


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        fragListTitles = listOf(
            getString(R.string.fav_tracks),
            getString(R.string.fav_playlists)
        )

        binding.viewPagerLibrary.adapter = ViewPageLibraryAdapter(
            childFragmentManager,
            lifecycle,
            fragList)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPagerLibrary) { tab, pos ->
            tab.text = fragListTitles[pos]
        }
        tabMediator.attach()


    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }


}