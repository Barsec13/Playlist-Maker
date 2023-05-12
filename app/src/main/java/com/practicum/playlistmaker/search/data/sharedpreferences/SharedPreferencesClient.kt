package com.practicum.playlistmaker.search.data.sharedpreferences

import com.practicum.playlistmaker.media.domain.model.Track

interface SharedPreferencesClient {
    fun addTrack(track: Track, position: Int)
    fun tracksHistoryFromJson(): List<Track>
    fun clearHistory()
    fun saveTrackForHistory(historyTracks: ArrayList<Track>)
}