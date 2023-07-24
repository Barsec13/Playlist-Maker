package com.practicum.playlistmaker.util

import com.practicum.playlistmaker.player.domain.model.Track

object CheckTrackInFavorites{
    fun checkTrackInFavorites(tracks: List<Track>, idFavoriteTracks: List<Int>): List<Track> {

        tracks.forEach{
            it.isFavorite = it.trackId in idFavoriteTracks
        }
        return tracks
    }
}