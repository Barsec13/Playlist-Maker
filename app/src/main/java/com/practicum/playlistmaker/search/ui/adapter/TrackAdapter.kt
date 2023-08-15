package com.practicum.playlistmaker.search.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.ui.view_holder.TrackViewHolder

class TrackAdapter(
    private val tracks: ArrayList<Track>, private val resolution: String,
) : RecyclerView.Adapter<TrackViewHolder>() {

    companion object {
        const val HIGH_RESOLUTION = "high resolution"
        const val LOW_RESOLUTION = "low resolution"
    }

    var itemClickListener: ((Int, Track) -> Unit)? = null
    var itemLongClickListener: ((Int, Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent, resolution)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener() {
            itemClickListener?.invoke(position, track)
        }
        holder.itemView.setOnLongClickListener(){
            itemLongClickListener?.invoke(position, track)
            return@setOnLongClickListener true
        }
    }

    override fun getItemCount(): Int {
        return tracks.size
    }

    fun setTracks(newTracks: List<Track>?) {
        tracks.clear()
        if (!newTracks.isNullOrEmpty()) {
            tracks.addAll(newTracks)
        }
        notifyDataSetChanged()
    }
}