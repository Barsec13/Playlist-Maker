package com.practicum.playlistmaker.search.data.sharedpreferences

import com.practicum.playlistmaker.player.domain.model.Track

interface SharedPreferencesSearchClient {
    fun addTrack(track: Track, position: Int)
    fun tracksHistoryFromJson(): List<Track>
    fun clearHistory()
    fun saveTrackForHistory(historyTracks: ArrayList<Track>)
}