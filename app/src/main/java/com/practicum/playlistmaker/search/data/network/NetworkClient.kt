package com.practicum.playlistmaker.search.data.network

interface NetworkClient {
    suspend fun loadTracks(
        searchText: String,
    ): ResultLoadTracks
}