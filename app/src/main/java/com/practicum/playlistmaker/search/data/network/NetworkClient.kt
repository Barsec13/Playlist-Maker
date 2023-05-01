package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.media.domain.model.Track

interface NetworkClient {
    fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: () -> Unit,
        serverError: () -> Unit,
        noInternet: () -> Unit,
    )
}