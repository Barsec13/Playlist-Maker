package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.domain.models.Track

class TrackResponse(val resultCount: Int,
                    val results: List<Track>) {
}