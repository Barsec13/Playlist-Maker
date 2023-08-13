package com.practicum.playlistmaker.playlist.domain.api

interface PlaylistRepository {
   fun getListIdTracks(idTracksInPlaylist: String): List<Int>
}