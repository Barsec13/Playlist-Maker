package com.practicum.playlistmaker.db.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface FavoriteTrackInteractor {
    suspend fun insertFavoriteTrack(tracks: Track)

    suspend fun deleteTrackOnFavorite(track: Track)

    fun getFavoriteTracks(): Flow<List<Track>>
}