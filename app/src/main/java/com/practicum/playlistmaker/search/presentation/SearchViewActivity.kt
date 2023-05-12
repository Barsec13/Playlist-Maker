package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.models.NetworkResponse
import com.practicum.playlistmaker.media.domain.model.Track

interface SearchViewActivity {
    fun refreshHistory(historyTracks: List<Track>)
    fun showHistoryList()
    fun hideHistoryList()
    fun hideMessageError()
    fun hideKeyboard()
    fun showTracks(tracks: List<Track>)
    fun showMessageError(networkResponse: NetworkResponse)
    fun clearTextSearch()
    fun showLoad()
    fun hideLoad()
}