package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.media.domain.model.Track

class TrackResponse(
    val resultCount: Int,
    val results: List<Track>,
)