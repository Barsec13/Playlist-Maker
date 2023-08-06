package com.practicum.playlistmaker.player.data.repository

import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.entity.FavoriteTrackEntity
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val trackDbConverter: TrackDbConverter,
) : PlayerRepository {
    override suspend fun getFavoriteTrackById(trackId: Int): Flow<Track> = flow {
        val track = appDataBase.favoriteTrackDao().getFavoriteTrackById(trackId) ?: return@flow
        emit(converterFromTrackEntity(track))
    }

    private fun converterFromTrackEntity(favoriteTrackEntity: FavoriteTrackEntity): Track {
        return trackDbConverter.map(favoriteTrackEntity)
    }

    override suspend fun checkFavorite(trackId: Int): Flow<Boolean> = flow {
        val idFavoriteTracks = appDataBase.favoriteTrackDao().getIdFavoriteTrack()
        emit (trackId in idFavoriteTracks)
    }
}