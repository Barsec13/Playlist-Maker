package com.practicum.playlistmaker.player.data.sharedpreferences

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.player.domain.model.Track

const val HISTORY_TRACKS_KEY = "history_tracks_key"

class SharedPreferencesPlayerClientImpl(
    private val sharedPref: SharedPreferences,
    private val gson: Gson) :
    SharedPreferencesPlayerClient {

    private val typeTokenArrayList = object : TypeToken<ArrayList<Track>>() {}.type

    override fun tracksHistoryFromJson(): List<Track> {
        val jsonHistoryTracks =
            sharedPref.getString(HISTORY_TRACKS_KEY, null) ?: return ArrayList<Track>()
        return gson.fromJson<ArrayList<Track>>(jsonHistoryTracks, typeTokenArrayList)
    }
}