package com.practicum.playlistmaker.edit_playlist.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.new_playlist.ui.view_model.NewPlaylistViewModel
import kotlinx.coroutines.launch

class EditPlaylistViewModel(
    private val playlistDbInteractor: PlaylistDbInteractor,
    private val newPlaylistInteractor: NewPlaylistInteractor,
    private val playlistId: Int
) : NewPlaylistViewModel(playlistDbInteractor, newPlaylistInteractor) {

    var playlist: Playlist? = null

    private val playlistStateLiveData = MutableLiveData<Playlist>()

    fun observeStatePlaylist(): LiveData<Playlist> = playlistStateLiveData

    fun getInfoPlaylist(){
        viewModelScope.launch {
            playlistDbInteractor.getPlaylistById(playlistId).collect { playlistById ->
                playlist = playlistById
                playlistStateLiveData.postValue(playlist!!)
                if (!playlist!!.uriCover.toString().isNullOrEmpty()) uriCover = playlist!!.uriCover
            }
        }
    }

    fun editPlaylistClicked(playlistName: String, playlistDescription: String) {
        viewModelScope.launch {
            playlistDbInteractor.updatePlaylist(
                Playlist(
                    id = playlistId,
                    playListName = playlistName,
                    playlistDescription = playlistDescription,
                    uriCover = uriCover,
                    tracksList = playlist!!.tracksList,
                    quantityTracks = playlist!!.quantityTracks
                )
            )
        }
    }
}