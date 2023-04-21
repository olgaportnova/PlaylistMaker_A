package com.example.playlistmaker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.example.playlistmaker.databinding.TrackViewBinding
import java.text.SimpleDateFormat
import java.util.*

class TrackAdapter(songs: ArrayList<Song>): RecyclerView.Adapter<TrackAdapter.TrackHolder>() {


    class TrackHolder (item: View):RecyclerView.ViewHolder(item) {

        val binding = TrackViewBinding.bind(item)
        val cornerRadius = binding.root.context.resources.getDimensionPixelSize(R.dimen.radius_art_work)
        fun bind (track: Song) = with(binding){
        trackName.text= track.trackName
            artistName.text = track.artistName
            trackTime.text=SimpleDateFormat("mm:ss", Locale.getDefault()).format(track.trackTimeMillis)
            Glide.with(itemView).load(track.artworkUrl100).transform(RoundedCorners(cornerRadius)).placeholder(R.drawable.placeholder).into(artWork)

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.track_view,parent,false)
        return TrackHolder(view)
    }

    override fun getItemCount(): Int {
        return songs.size
    }

    override fun onBindViewHolder(holder: TrackHolder, position: Int) {
        holder.bind(songs[position])

    }
}