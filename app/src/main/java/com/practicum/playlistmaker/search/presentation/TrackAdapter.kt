package com.practicum.playlistmaker.search.presentation

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.domain.models.Track

class TrackAdapter(
    private val tracks: ArrayList<Track>
) : RecyclerView.Adapter<TrackViewHolder> (){

    var itemClickListener: ((Int, Track) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TrackViewHolder {
        return TrackViewHolder(parent)
    }

    override fun onBindViewHolder(holder: TrackViewHolder, position: Int) {
        val track = tracks[position]
        holder.bind(track)
        holder.itemView.setOnClickListener(){
            itemClickListener?.invoke(position, track)
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