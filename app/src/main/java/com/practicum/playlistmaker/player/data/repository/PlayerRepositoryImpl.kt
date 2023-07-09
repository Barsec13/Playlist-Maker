package com.practicum.playlistmaker.player.data.repository

import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.entity.TrackEntity
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val trackDbConverter: TrackDbConverter,
) : PlayerRepository {
    override suspend fun getFavoriteTrackById(trackId: Int): Flow<Track> = flow {
        val track = appDataBase.trackDao().getFavoriteTrackById(trackId)
        emit(converterFromTrackEntity(track))
    }

    private fun converterFromTrackEntity(trackEntity: TrackEntity): Track {
        return trackDbConverter.map(trackEntity)
    }
}