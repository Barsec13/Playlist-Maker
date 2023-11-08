package com.practicum.playlistmaker.playlist.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {
    suspend fun getTrackInPlaylist(idTracksInPlaylist: String): Flow<List<Track>>
    fun sharePlaylist(message: String)
}