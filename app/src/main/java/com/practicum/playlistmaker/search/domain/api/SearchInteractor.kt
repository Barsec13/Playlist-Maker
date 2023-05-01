package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.media.domain.model.Track

interface SearchInteractor {
    fun clearHistory()

    fun tracksHistoryFromJson(): List<Track>

    fun addTrack(track: Track, position: Int)

    fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: () -> Unit,
        serverError: () -> Unit,
        noInternet: () -> Unit,
    )
}