package com.practicum.playlistmaker.db.data.impl

import com.google.gson.Gson
import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity
import com.practicum.playlistmaker.db.domain.api.PlayListRepository
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val gson: Gson,
) : PlayListRepository {
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDataBase.playlistDao().insertPlaylist(converterFromPlaylistEntity(playlist))
    }

    override suspend fun deletePlayList(playlist: Playlist) {
        appDataBase.playlistDao().deletePlayList(converterFromPlaylistEntity(playlist))
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDataBase.playlistDao().getPlaylists()
        emit(converterFromPlaylists(playlists))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDataBase.playlistDao().updatePlaylist(converterFromPlaylistEntity(playlist))
    }

    override suspend fun insertTrackInPlaylist(
        track: Track,
        playlist: Playlist,
        tracksId: ArrayList<Int>,
    ) {
        appDataBase.trackInPlaylistDao().insertTrackInPlaylist(converterFromTrack(track))
        tracksId.add(track.trackId)
        playlist.tracksList = gson.toJson(tracksId)
        playlist.quantityTracks += 1
        appDataBase.playlistDao().updatePlaylist(converterFromPlaylistEntity(playlist))
    }

    private fun converterFromPlaylistEntity(playlist: Playlist): PlaylistEntity {
        return playlistDbConverter.map(playlist)
    }

    private fun converterFromPlaylists(playlistEntitys: List<PlaylistEntity>): List<Playlist> {
        return playlistEntitys.map { playlistEntity -> playlistDbConverter.map(playlistEntity) }
    }


    private fun converterFromTrack(track: Track): TrackInPlaylistEntity {
        return playlistDbConverter.map(track)
    }
}