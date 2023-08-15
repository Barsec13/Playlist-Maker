package com.practicum.playlistmaker.db.data.converter

import android.annotation.SuppressLint
import com.practicum.playlistmaker.db.data.entity.FavoriteTrackEntity
import com.practicum.playlistmaker.player.domain.model.Track
import java.text.SimpleDateFormat
import java.util.Date

class TrackDbConverter {
    fun map(track: Track): FavoriteTrackEntity {
        return FavoriteTrackEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.artworkUrl60,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            getCurrentDate()
        )
    }

    fun map(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return Track(
            favoriteTrackEntity.id,
            favoriteTrackEntity.trackName,
            favoriteTrackEntity.artistName,
            favoriteTrackEntity.trackTimeMillis,
            favoriteTrackEntity.artworkUrl100,
            favoriteTrackEntity.artworkUrl60,
            favoriteTrackEntity.collectionName,
            favoriteTrackEntity.releaseDate,
            favoriteTrackEntity.primaryGenreName,
            favoriteTrackEntity.country,
            favoriteTrackEntity.previewUrl,

        )
    }

    @SuppressLint("SimpleDateFormat")
    private fun getCurrentDate(): String {
        val sdf = SimpleDateFormat("dd/M/yyyy hh:mm:ss")
        return sdf.format(Date())
    }
}