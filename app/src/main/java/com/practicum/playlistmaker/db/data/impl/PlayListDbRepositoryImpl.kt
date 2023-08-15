package com.practicum.playlistmaker.db.data.impl

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.db.data.converter.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.entity.PlaylistEntity
import com.practicum.playlistmaker.db.data.entity.TrackInPlaylistEntity
import com.practicum.playlistmaker.db.domain.api.PlayListDbRepository
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayListDbRepositoryImpl(
    private val appDataBase: AppDataBase,
    private val playlistDbConverter: PlaylistDbConverter,
    private val gson: Gson,
) : PlayListDbRepository {

    private val typeTokenArrayList = object : TypeToken<ArrayList<Int>>() {}.type
    override suspend fun insertPlaylist(playlist: Playlist) {
        appDataBase.playlistDao().insertPlaylist(converterFromPlaylistEntity(playlist))
    }

    override suspend fun deletePlayList(playlist: Playlist) {
        appDataBase.playlistDao().deletePlayList(converterFromPlaylistEntity(playlist))
        val trackIdInPlaylist =
            gson.fromJson<ArrayList<Int>>(playlist!!.tracksList, typeTokenArrayList)
                ?: arrayListOf()
        trackIdInPlaylist.forEach(){trackId ->
            checkDeleteTrackFromAllPlaylist(trackId)
        }
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> = flow {
        val playlists = appDataBase.playlistDao().getPlaylists()
        emit(converterFromPlaylists(playlists))
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        appDataBase.playlistDao().updatePlaylist(converterFromPlaylistEntity(playlist))
    }

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> = flow {
        val playlist = appDataBase.playlistDao().getPlaylistById(playlistId)
        emit(converterFromPlaylist(playlist))
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

    private fun converterFromPlaylists(playlistEntities: List<PlaylistEntity>): List<Playlist> {
        return playlistEntities.map { playlistEntity -> playlistDbConverter.map(playlistEntity) }
    }


    private fun converterFromTrack(track: Track): TrackInPlaylistEntity {
        return playlistDbConverter.map(track)
    }

    private fun converterFromPlaylist(playlistEntity: PlaylistEntity): Playlist {
        return playlistDbConverter.map(playlistEntity)
    }

    override suspend fun getAllTrackInPlaylists(): Flow<List<Track>> = flow {
        val allTrackInPlaylist =
            appDataBase.trackInPlaylistDao().getAllTrackInPlaylists()
                .sortedWith(compareBy(TrackInPlaylistEntity::currentDate))
                .map { trackInPlaylistEntity ->
                    playlistDbConverter.map(trackInPlaylistEntity)
                }

        emit(allTrackInPlaylist)
    }

    override suspend fun deleteTrackInPlaylist(playlist: Playlist?, trackId: Int) {
        val trackIdInPlaylist =
            gson.fromJson<ArrayList<Int>>(playlist!!.tracksList, typeTokenArrayList)
                ?: arrayListOf()
        trackIdInPlaylist.removeIf { it == trackId }
        playlist.tracksList = gson.toJson(trackIdInPlaylist)
        playlist.quantityTracks -= 1
        appDataBase.playlistDao().updatePlaylist(converterFromPlaylistEntity(playlist))
        checkDeleteTrackFromAllPlaylist(trackId)
    }

    private suspend fun checkDeleteTrackFromAllPlaylist(trackId: Int) {
        val playlists = appDataBase.playlistDao().getPlaylists()
        playlists.forEach() { playlistEntity ->
            if (playlistEntity.tracksList.isNullOrEmpty()) return@forEach
            val tracksInPlaylist =
                gson.fromJson<ArrayList<Int>>(playlistEntity.tracksList, typeTokenArrayList)
            if (trackId in tracksInPlaylist) return
        }
        appDataBase.trackInPlaylistDao()
            .deleteTrack(appDataBase.trackInPlaylistDao().getPlaylistById(trackId))
    }
}