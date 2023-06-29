package com.practicum.playlistmaker.player.data.sharedpreferences

import com.practicum.playlistmaker.player.domain.model.Track

interface SharedPreferencesPlayerClient {
    fun tracksHistoryFromJson(): List<Track>
}