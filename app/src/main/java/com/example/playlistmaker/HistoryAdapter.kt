package com.example.playlistmaker

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.data.dto.TrackDto
import com.example.playlistmaker.databinding.TrackViewBinding
import java.text.SimpleDateFormat
import java.util.*

class HistoryAdapter(var tracks: Array<TrackDto>, private val listener: Listener) :
    RecyclerView.Adapter<HistoryAdapter.HistoryHolder>() {

    class HistoryHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val binding = TrackViewBinding.bind(itemView)
        val cornerRadius =
            binding.root.context.resources.getDimensionPixelSize(R.dimen.radius_art_work)

        fun bind(track: TrackDto, listener: Listener) = with(binding) {
            trackName.text = track.trackName
            artistName.text = track.artistName
            trackTime.text =
                SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            Glide.with(itemView).load(track.artworkUrl100).transform(RoundedCorners(cornerRadius))
                .placeholder(R.drawable.placeholder).into(artWork)
            itemView.setOnClickListener {
                Log.d("HISTORY_CLICK", "clicked")
                listener.onClick(track)
            }

        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view, parent, false)
        return HistoryHolder(view)
    }

    override fun onBindViewHolder(holder: HistoryHolder, position: Int) {
        holder.bind(tracks[position], listener)
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    interface Listener {
        fun onClick(track: TrackDto)
    }


}