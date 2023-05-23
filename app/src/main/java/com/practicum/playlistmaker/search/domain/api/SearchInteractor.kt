package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.models.NetworkError

interface SearchInteractor {
    fun clearHistory()

    fun tracksHistoryFromJson(): List<Track>

    fun addTrack(track: Track, position: Int)

    fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: (NetworkError) -> Unit,
        serverError: (NetworkError) -> Unit,
        noInternet: (NetworkError) -> Unit,
    )
}