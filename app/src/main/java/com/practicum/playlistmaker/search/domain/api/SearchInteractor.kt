package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun clearHistory()

    suspend fun tracksHistoryFromJson(): List<Track>

    fun addTrack(track: Track, position: Int)

    fun loadTracks(
        searchText: String,
    ): Flow<ResultLoadTracks>
}