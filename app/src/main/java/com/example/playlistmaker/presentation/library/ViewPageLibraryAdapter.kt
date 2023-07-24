package com.example.playlistmaker.presentation.library

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPageLibraryAdapter (libraryActivity: LibraryActivity, private val list: List<Fragment>): FragmentStateAdapter(libraryActivity) {


    override fun getItemCount(): Int {
       return list.size
    }

    override fun createFragment(position: Int): Fragment {
        return list[position]
    }
}