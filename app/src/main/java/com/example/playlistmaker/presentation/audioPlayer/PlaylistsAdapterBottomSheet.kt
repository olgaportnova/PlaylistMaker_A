package com.example.playlistmaker.presentation.audioPlayer

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.PlaylistViewSmallBinding
import com.example.playlistmaker.domain.model.Playlist

class PlaylistsAdapterBottomSheet(
    private val context: Context,
    private val playlistsList: MutableList<Playlist>,
    private val listener: Listener
) : RecyclerView.Adapter<PlaylistsAdapterBottomSheet.PlaylistHolderSmall>() {



    inner class PlaylistHolderSmall(private val binding: PlaylistViewSmallBinding) :

        RecyclerView.ViewHolder(binding.root) {
        private val cornerRadius =
            context.resources.getDimensionPixelSize(R.dimen.radius_art_work)

        fun bind(playlist: Playlist) {
            binding.apply {
                playlistName.text = playlist.name
                numberOfTracks.text = "${playlist.numberOfTracks} ${getTrackWordForm(playlist.numberOfTracks?:0)}"
                Glide.with(root.context)
                    .load(playlist.imagePath)
                    .transform(RoundedCorners(cornerRadius))
                    .placeholder(R.drawable.placeholder_big)
                    .into(playlistCoverImage)
                root.setOnClickListener { listener.onClick(playlist) }
            }
        }
    }

    fun getTrackWordForm(count: Int): String {
        return when {
            count % 100 in 11..14 -> "треков"
            count % 10 == 1 -> "трек"
            count % 10 in 2..4 -> "трека"
            else -> "треков"
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistHolderSmall {
        val binding = PlaylistViewSmallBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PlaylistHolderSmall(binding)
    }

    override fun getItemCount(): Int = playlistsList.size

    override fun onBindViewHolder(holder: PlaylistHolderSmall, position: Int) {
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

}
