package com.practicum.playlistmaker.new_playlist.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistsRepository
import kotlinx.coroutines.flow.Flow

class NewPlaylistInteractorImpl(private val newPlaylistsRepository: NewPlaylistsRepository): NewPlaylistInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri, albumName: String): Flow<Uri> {
        return newPlaylistsRepository.saveImageToPrivateStorage(uri, albumName)
    }
}