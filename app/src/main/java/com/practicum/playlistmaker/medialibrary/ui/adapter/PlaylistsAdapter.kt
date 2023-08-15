package com.practicum.playlistmaker.medialibrary.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.medialibrary.ui.view_holder.PlaylistsViewHolder
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist

class PlaylistsAdapter(
    private val playlists: ArrayList<Playlist>,
) : RecyclerView.Adapter<PlaylistsViewHolder>() {

    var itemClickListener: ((Int, Playlist) -> Unit)? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsViewHolder {
        return PlaylistsViewHolder(parent)
    }

    override fun getItemCount(): Int {
       return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistsViewHolder, position: Int) {
        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener(){
            itemClickListener?.invoke(position, playlist)
        }
    }

    fun setPlaylists(newPlaylists: List<Playlist>?) {
        playlists.clear()
        if (!newPlaylists.isNullOrEmpty()) {
            playlists.addAll(newPlaylists)
        }
        notifyDataSetChanged()
    }
}