package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesSearchClient
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import kotlinx.coroutines.flow.Flow

class SearchInteractorImpl(
    private val sharedPreferencesSearchClient: SharedPreferencesSearchClient,
    private val searchRepository: SearchRepository,
) : SearchInteractor {
    override fun clearHistory() {
        sharedPreferencesSearchClient.clearHistory()
    }

    override suspend fun tracksHistoryFromJson(): List<Track> {
        return sharedPreferencesSearchClient.tracksHistoryFromJson()
    }

    override fun addTrack(track: Track, position: Int) {
        sharedPreferencesSearchClient.addTrack(track, position)
    }

    override fun loadTracks(
        searchText: String,
    ): Flow<ResultLoadTracks> {
        return searchRepository.loadTracks(searchText)
    }
}