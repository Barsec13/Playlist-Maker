package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.api.FavoriteTrackInteractor
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class FavoriteTrackInteractorImpl(
    private val favoriteTrackRepository: FavoriteTrackRepository,
) : FavoriteTrackInteractor {
    override suspend fun insertFavoriteTrack(track: Track) {
        favoriteTrackRepository.insertFavoriteTrack(track)
    }

    override suspend fun deleteTrackOnFavorite(track: Track) {
        favoriteTrackRepository.deleteTrackOnFavorite(track)
    }

    override fun getFavoriteTracks(): Flow<List<Track>>{
        return favoriteTrackRepository.getFavoriteTracks()
    }
}