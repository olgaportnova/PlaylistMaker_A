package com.example.playlistmaker.presentation.library.playlists

import android.content.Context
import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewBinding
import com.example.playlistmaker.domain.model.Playlist

class PlaylistsAdapter(
    private val context: Context,
    private val playlistsList: MutableList<Playlist>,
    private val listener: Listener
) : RecyclerView.Adapter<PlaylistsAdapter.PlaylistHolder>() {

    val cornerRadius = context.resources.getDimensionPixelSize(R.dimen.radius_playlist_cover)


    inner class PlaylistHolder(private val binding: PlaylistViewBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(playlist: Playlist) {
            binding.apply {
                val placeholderRes = if (isNightModeActive(root.context)) {
                    R.drawable.placeholder_dark
                } else {
                    R.drawable.placeholder_light
                }
                playlistName.text = playlist.name
                numberOfTracks.text = "${playlist.numberOfTracks.toString()} ${getTrackWordForm(playlist.numberOfTracks?:0)}"
                Glide.with(root.context)
                    .load(playlist.imagePath)
                   // .transform(RoundedCorners(8dp))
                    .placeholder(placeholderRes)
                    .into(playlistcover)
                root.setOnClickListener { listener.onClick(playlist) }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolder {
        val binding = PlaylistViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistHolder(binding)
    }

    override fun getItemCount(): Int = playlistsList.size

    override fun onBindViewHolder(holder: PlaylistHolder, position: Int) {
        holder.bind(playlistsList[position])
    }

    fun updateData(newPlaylistsList: List<Playlist>) {
        playlistsList.clear()
        playlistsList.addAll(newPlaylistsList)
        notifyDataSetChanged()
    }

    interface Listener {
        fun onClick(playlist: Playlist)
    }

    fun getTrackWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }

    fun isNightModeActive(context: Context): Boolean {
        val defaultNightMode = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return defaultNightMode == Configuration.UI_MODE_NIGHT_YES
    }


}
