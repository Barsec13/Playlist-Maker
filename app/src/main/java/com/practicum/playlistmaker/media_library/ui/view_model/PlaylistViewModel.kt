package com.practicum.playlistmaker.media_library.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.ui.models.PlaylistStateInterface
import kotlinx.coroutines.launch

class PlaylistViewModel(private val playlistInteractor: PlaylistInteractor) : ViewModel() {

    private val stateLiveData = MutableLiveData<PlaylistStateInterface>()

    fun observeState(): LiveData<PlaylistStateInterface> = stateLiveData

    private fun renderState(state: PlaylistStateInterface) {
        stateLiveData.postValue(state)
    }

    fun showPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect(){
                playlists ->
                if (playlists.isEmpty()) renderState(PlaylistStateInterface.PlaylistsIsEmpty)
                else renderState(PlaylistStateInterface.Playlists(playlists = playlists))
            }
        }
    }

}