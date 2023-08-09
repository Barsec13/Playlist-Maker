package com.practicum.playlistmaker.new_playlist.ui.view_model

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import kotlinx.coroutines.launch

class NewPlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val newPlaylistInteractor: NewPlaylistInteractor,
) : ViewModel() {

    private val coverLiveData = MutableLiveData<Uri>()
    fun observeStateCover(): LiveData<Uri> = coverLiveData

    private var uriCover: Uri = "".toUri()


    fun createPlaylistClicked(playlistName: String, playlistDescription: String) {
        viewModelScope.launch {
            playlistInteractor.insertPlaylist(
                Playlist(
                    id = 0,
                    playListName = playlistName,
                    playlistDescription = playlistDescription,
                    uriCover = uriCover,
                )
            )
        }
    }

    fun saveImage(albumName: String) {
        viewModelScope.launch {
            newPlaylistInteractor.saveImageToPrivateStorage(uriCover, albumName).collect() { uri ->
                uriCover = uri
            }
        }
    }

    fun saveUriImage(uri: Uri) {
        uriCover = uri
    }

    fun showCover() {
        if (uriCover.toString().isNullOrEmpty()) return
        else coverLiveData.postValue(uriCover)
    }
}