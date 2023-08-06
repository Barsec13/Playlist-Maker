package com.practicum.playlistmaker.player.ui.view_holder

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.util.GetWordTrack

class PlaylistViewHolderBottomSheet(parentView: ViewGroup) : RecyclerView.ViewHolder(
    LayoutInflater.from(parentView.context)
        .inflate(R.layout.playlist_view_on_player, parentView, false)
) {
    private val coverPlaylist: ImageView = itemView.findViewById(R.id.cover_playlist)
    private val namePlaylist: TextView = itemView.findViewById(R.id.name_playlist)
    private val countTracks: TextView = itemView.findViewById(R.id.count_tracks)

    fun bind(model: Playlist) {
        coverPlaylist.setImageURI(model.uriCover)
        namePlaylist.text = model.playListName
        val quantityTracks = model.quantityTracks
        countTracks.text = quantityTracks.toString() + " " + GetWordTrack.getWordTrack(quantityTracks, itemView)
    }
}