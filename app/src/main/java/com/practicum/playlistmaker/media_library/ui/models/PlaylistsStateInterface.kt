package com.practicum.playlistmaker.media_library.ui.models

import com.practicum.playlistmaker.new_playlist.domain.model.Playlist

interface PlaylistsStateInterface {

    object PlaylistsIsEmpty : PlaylistsStateInterface

    data class Playlists(
        val playlists: List<Playlist>,
    ) : PlaylistsStateInterface
}