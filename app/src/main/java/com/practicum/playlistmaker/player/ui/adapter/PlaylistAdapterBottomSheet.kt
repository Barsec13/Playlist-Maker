package com.practicum.playlistmaker.player.ui.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist
import com.practicum.playlistmaker.player.ui.view_holder.PlaylistViewHolderBottomSheet

class PlaylistAdapterBottomSheet(
    private val playlists: ArrayList<Playlist>,
    //private val gson: Gson,
) : RecyclerView.Adapter<PlaylistViewHolderBottomSheet>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PlaylistViewHolderBottomSheet {
        return PlaylistViewHolderBottomSheet(parent)
    }

    private val gson = Gson()

    var itemClickListener: ((Int, List<Int>, Playlist) -> Unit)? = null

    override fun getItemCount(): Int {
        return playlists.size
    }

    override fun onBindViewHolder(holder: PlaylistViewHolderBottomSheet, position: Int) {
        val typeTokenArrayList = object : TypeToken<ArrayList<Int>>() {}.type
        val tracksId =
            gson.fromJson<ArrayList<Int>>(playlists[position].tracksList, typeTokenArrayList) ?: arrayListOf()

        val playlist = playlists[position]
        holder.bind(playlist)
        holder.itemView.setOnClickListener() {
            itemClickListener?.invoke(position, tracksId, playlist)
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