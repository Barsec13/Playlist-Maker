package com.practicum.playlistmaker.db.domain.impl

import com.practicum.playlistmaker.db.domain.api.PlayListRepository
import com.practicum.playlistmaker.db.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(
    private val playListRepository: PlayListRepository,
) :
    PlaylistInteractor {
    override suspend fun insertPlaylist(playlist: Playlist) {
        playListRepository.insertPlaylist(playlist)
    }

    override suspend fun deletePlayList(playlist: Playlist) {
        playListRepository.deletePlayList(playlist)
    }

    override suspend fun getPlaylists(): Flow<List<Playlist>> {
        return playListRepository.getPlaylists()
    }

    override suspend fun updatePlaylist(playlist: Playlist) {
        playListRepository.updatePlaylist(playlist)
    }

    override suspend fun insertTrackInPlaylist(track: Track, playlist: Playlist, tracksId: List<Int>){
        playListRepository.insertTrackInPlaylist(track, playlist, tracksId as ArrayList<Int>)
    }
}