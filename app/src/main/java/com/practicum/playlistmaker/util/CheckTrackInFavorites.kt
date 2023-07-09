package com.practicum.playlistmaker.util

import com.practicum.playlistmaker.player.domain.model.Track

object CheckTrackInFavorites{
    fun checkTrackInFavorites(Tracks: List<Track>, idFavoriteTracks: List<Int>): List<Track> {
        Tracks.map { track ->
            if(track.trackId in idFavoriteTracks)
                track.isFavorite = true
        }
        return Tracks
    }
}