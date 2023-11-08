package com.practicum.playlistmaker.newplaylist.domain.model

import android.net.Uri

data class Playlist(
    val id: Int,
    val playListName: String,
    val playlistDescription: String,
    val uriCover: Uri,
    var tracksList: String? = null,
    var quantityTracks: Int = 0,
)