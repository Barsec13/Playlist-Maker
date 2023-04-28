package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.domain.models.Track

interface NetworkClient {
    fun loadTracks(searchText: String,
                   onSuccess: (List<Track>) -> Unit,
                   noData: () -> Unit,
                   serverError: () -> Unit,
                   noInternet: () -> Unit)
}