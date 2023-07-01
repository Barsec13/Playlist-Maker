package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.network.ResultLoadTracks
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesSearchClient
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class SearchInteractorImpl(
    private val sharedPreferencesSearchClient: SharedPreferencesSearchClient,
    private val searchRepository: SearchRepository,
) : SearchInteractor {
    override fun clearHistory() {
        sharedPreferencesSearchClient.clearHistory()
    }

    override fun tracksHistoryFromJson(): List<Track> {
        return sharedPreferencesSearchClient.tracksHistoryFromJson()
    }

    override fun addTrack(track: Track, position: Int) {
        sharedPreferencesSearchClient.addTrack(track, position)
    }

    override fun loadTracks(
        searchText: String,
    ): Flow<ResultLoadTracks> {
        return searchRepository.loadTracks(searchText).map { result ->
            when (result) {
                is ResultLoadTracks.OnSuccess -> ResultLoadTracks.OnSuccess(result.data!!)
                is ResultLoadTracks.NoData -> ResultLoadTracks.NoData()
                is ResultLoadTracks.NoInternet -> ResultLoadTracks.NoInternet()
                is ResultLoadTracks.ServerError -> ResultLoadTracks.ServerError()
            }
        }
    }
}