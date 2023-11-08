package com.practicum.playlistmaker.playlist.ui.models

interface SharePlaylistStateInterface {
    object playlistIsEmpity : SharePlaylistStateInterface

    data class messageTrackInPlaylist(val message: String) : SharePlaylistStateInterface
}