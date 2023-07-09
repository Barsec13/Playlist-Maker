package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    suspend fun getFavoriteTrackById(strackId: Int): Flow<Track>
}