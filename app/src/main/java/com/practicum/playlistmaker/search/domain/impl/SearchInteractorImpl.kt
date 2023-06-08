package com.practicum.playlistmaker.search.domain.impl

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesSearchClient
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.NetworkError

class SearchInteractorImpl(
    private val sharedPreferencesSearchClient: SharedPreferencesSearchClient,
    private val networkClient: NetworkClient,
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
        onSuccess: (List<Track>) -> Unit,
        noData: (NetworkError) -> Unit,
        serverError: (NetworkError) -> Unit,
        noInternet: (NetworkError) -> Unit,
    ) {
        networkClient.loadTracks(searchText, onSuccess, noData, serverError, noInternet)
    }


}