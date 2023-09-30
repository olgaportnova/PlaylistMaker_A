package com.example.playlistmaker.presentation.search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.R
import com.example.playlistmaker.databinding.TrackViewBinding
import com.example.playlistmaker.domain.model.Track
import java.text.SimpleDateFormat
import java.util.*



class TrackAdapter(var tracks: ArrayList<Track>, var onItemClickListener: OnItemClickListener, val onItemLongClickListener: OnItemLongClickListener): RecyclerView.Adapter<TrackAdapter.TrackHolder>() {


    class TrackHolder(item: View) : RecyclerView.ViewHolder(item) {

        val binding = TrackViewBinding.bind(item)
        val cornerRadius =
            item.resources.getDimensionPixelSize(R.dimen.radius_art_work)

        fun bind(track: Track, onItemClickListener: OnItemClickListener, onItemLongClickListener: OnItemLongClickListener) = with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis.toInt())
            Glide.with(itemView).load(track.artworkUrl100).transform(RoundedCorners(cornerRadius))
                .placeholder(R.drawable.placeholder).into(artWork)
            itemView.setOnClickListener {
                onItemClickListener?.onItemClick(track)
            }
            itemView.setOnLongClickListener {
                onItemLongClickListener?.onItemLongClick(track) ?: false
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(tracks[position],onItemClickListener, onItemLongClickListener)

    }

    interface OnItemClickListener {
        fun onItemClick(track: Track)
    }

    interface OnItemLongClickListener {
        fun onItemLongClick(track: Track): Boolean
    }

}