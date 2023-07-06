package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks

interface NetworkClient {
    suspend fun loadTracks(
        searchText: String,
    ): ResultLoadTracks
}