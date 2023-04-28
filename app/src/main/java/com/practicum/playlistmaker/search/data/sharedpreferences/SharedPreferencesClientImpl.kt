package com.practicum.playlistmaker.search.data.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.models.Track

const val HISTORY_TRACKS_KEY = "history_tracks_key"

class SearchHistoryImpl (private val sharedPref: SharedPreferences) : SharedPreferencesClient {

    private val typeTokenArrayList = object : TypeToken<ArrayList<Track>>() {}.type

    override fun addTrack(track: Track, position: Int){
        val jsonHistoryTracks = sharedPref.getString(HISTORY_TRACKS_KEY, null)
        if (jsonHistoryTracks == null){
            sharedPref.edit().putString(HISTORY_TRACKS_KEY, Gson().toJson(listOf(track))).apply()
            return
        }

        val historyTracks = Gson().fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)

        if (historyTracks.find { it.trackId == track.trackId } != null){
            historyTracks.remove(track)
            historyTracks.add(0, track)
            saveTrackForHistory(historyTracks)
            return
        }

        if (historyTracks.size == 10){
            historyTracks.remove(historyTracks[9])
        }

        historyTracks.add(0, track)
        saveTrackForHistory(historyTracks)
    }

    override fun tracksHistoryFromJson(): List<Track> {
        val jsonHistoryTracks = sharedPref.getString(HISTORY_TRACKS_KEY, null) ?: return ArrayList<Track>()
        return Gson().fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)
    }

    override fun clearHistory(){
        sharedPref.edit().remove(HISTORY_TRACKS_KEY).apply()
    }

    override fun saveTrackForHistory(historyTracks : ArrayList<Track>){
        sharedPref.edit().putString(HISTORY_TRACKS_KEY, Gson().toJson(historyTracks)).apply()
    }
}