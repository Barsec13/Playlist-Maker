package com.practicum.playlistmaker.media_library.ui.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.util.GetWord

class PlaylistsViewHolder(parentView: ViewGroup): RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_view, parentView, false)
) {
    private val coverPlaylist: ImageView = itemView.findViewById(R.id.cover_playlist)
    private val namePlaylist: TextView = itemView.findViewById(R.id.name_playlist)
    private val countTracks: TextView = itemView.findViewById(R.id.count_tracks)

    fun bind(model: Playlist){
        if (!model.uriCover.toString().isNullOrEmpty()) coverPlaylist.setImageURI(model.uriCover)
        namePlaylist.text = model.playListName
        val quantityTracks = model.quantityTracks
        countTracks.text = quantityTracks.toString() + " " + GetWord.getWordTrack(quantityTracks, itemView)
    }
}