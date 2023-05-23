package com.practicum.playlistmaker.search.ui.models

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.models.NetworkError

sealed interface SearchStateInterface {
    object Loading : SearchStateInterface

    data class HistoryTracks(
        val tracks: List<Track>,
    ) : SearchStateInterface

    object changeTextSearch : SearchStateInterface

    data class SearchTracks(
        val tracks: List<Track>,
    ) : SearchStateInterface

    data class Error(
        val error: NetworkError,
    ) : SearchStateInterface
}