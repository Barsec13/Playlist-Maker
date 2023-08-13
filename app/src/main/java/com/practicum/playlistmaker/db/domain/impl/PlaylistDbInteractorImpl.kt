package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.api.PlayListDbRepository
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistDbInteractorImpl(
    private val playListDbRepository: PlayListDbRepository,
) :
    PlaylistDbInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playListDbRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlayList(playlist: Playlist) {
        playListDbRepository.deletePlayList(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playListDbRepository.getPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playListDbRepository.updatePlaylist(playlist)
    }

    override suspend fun getPlaylistById(playlistId: Int): Flow<Playlist> {
        return playListDbRepository.getPlaylistById(playlistId)
    }

    override suspend fun deletaTrackInPlaylist(playlist: Playlist?, trackId: Int) {
        playListDbRepository.deleteTrackInPlaylist(playlist, trackId)
    }

    override suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist, tracksId: List<Int>){
        playListDbRepository.insertTrackInPlaylist(track, playlist, tracksId as ArrayList<Int>)
    }
}