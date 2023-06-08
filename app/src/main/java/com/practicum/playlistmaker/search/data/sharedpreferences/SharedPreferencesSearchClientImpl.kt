package com.practicum.playlistmaker.search.data.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.model.Track

const val HISTORY_TRACKS_KEY = "history_tracks_key"

class SharedPreferencesSearchClientImpl(
    private val sharedPref: SharedPreferences,
    private val gson: Gson) :
    SharedPreferencesSearchClient {

    private val typeTokenArrayList = object : TypeToken<ArrayList<Track>>() {}.type

    override fun addTrack(track: Track, position: Int) {
        val jsonHistoryTracks = sharedPref.getString(HISTORY_TRACKS_KEY, null)
        if (jsonHistoryTracks == null) {
            sharedPref.edit().putString(HISTORY_TRACKS_KEY, gson.toJson(listOf(track))).apply()
            return
        }

        val historyTracks = gson.fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)

        if (historyTracks.find { it.trackId == track.trackId } != null) {
            historyTracks.remove(track)
            historyTracks.add(0, track)
            saveTrackForHistory(historyTracks)
            return
        }

        if (historyTracks.size == 10) {
            historyTracks.remove(historyTracks[9])
        }

        historyTracks.add(0, track)
        saveTrackForHistory(historyTracks)
    }

    override fun tracksHistoryFromJson(): List<Track> {
        val jsonHistoryTracks =
            sharedPref.getString(HISTORY_TRACKS_KEY, null) ?: return ArrayList<Track>()
        return gson.fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)
    }

    override fun clearHistory() {
        sharedPref.edit().remove(HISTORY_TRACKS_KEY).apply()
    }

    override fun saveTrackForHistory(historyTracks: ArrayList<Track>) {
        sharedPref.edit().putString(HISTORY_TRACKS_KEY, gson.toJson(historyTracks)).apply()
    }
}