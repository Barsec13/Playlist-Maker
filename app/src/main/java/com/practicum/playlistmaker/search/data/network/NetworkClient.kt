package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.models.NetworkError

interface NetworkClient {
    fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: (NetworkError) -> Unit,
        serverError: (NetworkError) -> Unit,
        noInternet: (NetworkError) -> Unit,
    )
}