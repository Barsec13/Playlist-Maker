package com.practicum.playlistmaker.medialibrary.ui.models

import com.practicum.playlistmaker.newplaylist.domain.model.Playlist

interface PlaylistsStateInterface {

    object PlaylistsIsEmpty : PlaylistsStateInterface

    data class Playlists(
        val playlists: List<Playlist>,
    ) : PlaylistsStateInterface
}