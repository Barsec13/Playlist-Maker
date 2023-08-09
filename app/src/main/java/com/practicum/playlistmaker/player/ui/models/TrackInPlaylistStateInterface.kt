package com.practicum.playlistmaker.player.ui.models

interface TrackInPlaylistStateInterface {

    data class TrackOnPlaylist(val namePlaylist: String) : TrackInPlaylistStateInterface

    data class TrackAddToPlaylist(val namePlaylist: String) : TrackInPlaylistStateInterface
}