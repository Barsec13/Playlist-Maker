package com.practicum.playlistmaker.search.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media.domain.model.Track

class TrackHistoryAdapter(
    private var tracksHistory: ArrayList<Track>,
) : RecyclerView.Adapter<TrackViewHolder>() {

    var itemClickListener: ((Int, Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracksHistory[position]
        holder.bind(track)
        holder.itemView.setOnClickListener() {
            itemClickListener?.invoke(position, track)
        }
    }

    override fun getItemCount(): Int {
        return tracksHistory.size
    }

    fun setTracks(newTracks: List<Track>?) {
        tracksHistory.clear()
        if (!newTracks.isNullOrEmpty()) {
            tracksHistory.addAll(newTracks)
        }
        notifyDataSetChanged()
    }
}