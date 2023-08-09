package com.practicum.playlistmaker.media_library.ui.models

import com.practicum.playlistmaker.new_playlist.domain.model.Playlist

interface PlaylistStateInterface {

    object PlaylistsIsEmpty : PlaylistStateInterface

    data class Playlists(
        val playlists: List<Playlist>,
    ) : PlaylistStateInterface
}