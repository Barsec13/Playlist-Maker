package com.practicum.playlistmaker.media_library.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.media_library.ui.view_holder.PlaylistViewHolder
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist

class PlaylistAdapter(
    private val playlists: ArrayList<Playlist>,
) : RecyclerView.Adapter<PlaylistViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistViewHolder {
        return PlaylistViewHolder(parent)
    }

    override fun getItemCount(): Int {
       return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
    }

    fun setPlaylists(newPlaylists: List<Playlist>?) {
        playlists.clear()
        if (!newPlaylists.isNullOrEmpty()) {
            playlists.addAll(newPlaylists)
        }
        notifyDataSetChanged()
    }
}