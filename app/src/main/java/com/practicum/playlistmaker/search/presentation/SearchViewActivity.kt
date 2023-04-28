package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.models.NetworkError
import com.practicum.playlistmaker.domain.models.Track

interface SearchViewActivity {
    fun refreshHistory(historyTracks: List<Track>)
    fun showHistoryList()
    fun hideHistoryList()
    fun hideMessageError()
    fun hideKeyboard()
    fun showTracks(tracks: List<Track>)
    fun showMessageError(networkError: NetworkError)
    fun clearTextSearch()
    fun showLoad()
    fun hideLoad()
}