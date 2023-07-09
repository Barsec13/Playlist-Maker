package com.practicum.playlistmaker.db.data.impl

import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.entity.TrackEntity
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
            .trackDao()
            .insertFavoriteTrack(
                converterFromTrack(track)
            )
    }

    override suspend fun deleteTrackOnFavorite(track: Track) {
        appDataBase
            .trackDao()
            .deleteTrackOnFavorite(
                converterFromTrack(track)
            )
    }

    override fun getFavoriteTracks(): Flow<List<Track>> = flow {
        val tracks = appDataBase.trackDao().getFavoriteTracks()
        emit(converterFromTrackEntity(tracks))
    }

    private fun converterFromTrackEntity(tracksEntity: List<TrackEntity>): List<Track> {
        return tracksEntity.map { trackEntity -> trackDbConverter.map(trackEntity) }
    }

    private fun converterFromTrack(track: Track): TrackEntity {
        return trackDbConverter.map(track)
        //return tracks.map { track -> trackDbConverter.map(track) }
    }
}