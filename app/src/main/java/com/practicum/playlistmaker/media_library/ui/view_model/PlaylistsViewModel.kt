package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.media_library.ui.models.PlaylistsStateInterface
import kotlinx.coroutines.launch

class PlaylistsViewModel(private val playlistDbInteractor: PlaylistDbInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistsStateInterface>()

    fun observeState(): LiveData<PlaylistsStateInterface> = stateLiveData

    private fun renderState(state: PlaylistsStateInterface) {
        stateLiveData.postValue(state)
    }

    fun showPlaylist() {
        viewModelScope.launch {
            playlistDbInteractor.getPlaylists().collect(){
                playlists ->
                if (playlists.isEmpty()) renderState(PlaylistsStateInterface.PlaylistsIsEmpty)
                else renderState(PlaylistsStateInterface.Playlists(playlists = playlists))
            }
        }
    }
}