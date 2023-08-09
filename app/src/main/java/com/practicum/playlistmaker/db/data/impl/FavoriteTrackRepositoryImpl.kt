package com.practicum.playlistmaker.db.data.impl

import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.entity.FavoriteTrackEntity
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackRepository
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class FavoriteTrackRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val trackDbConverter: TrackDbConverter,
) : FavoriteTrackRepository {
    override suspend fun insertFavoriteTrack(track: Track) {
        appDataBase
            .favoriteTrackDao()
            .insertFavoriteTrack(
                converterFromTrack(track)
            )
    }

    override suspend fun deleteTrackOnFavorite(track: Track) {
        appDataBase
            .favoriteTrackDao()
            .deleteTrackOnFavorite(
                converterFromTrack(track)
            )
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDataBase.favoriteTrackDao()
            .getFavoriteTracks()
            .sortedWith(compareBy(FavoriteTrackEntity::currentDate))
        emit(converterFromTrackEntity(tracks))
    }

    private fun converterFromTrackEntity(tracksEntity: List<FavoriteTrackEntity>): List<Track> {
        return tracksEntity.map { trackEntity -> trackDbConverter.map(trackEntity) }
    }

    private fun converterFromTrack(track: Track): FavoriteTrackEntity {
        return trackDbConverter.map(track)
    }
}