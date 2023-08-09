package com.practicum.playlistmaker.new_playlist.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistRepository
import kotlinx.coroutines.flow.Flow

class NewPlaylistInteractorImpl(private val newPlaylistRepository: NewPlaylistRepository): NewPlaylistInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri, albumName: String): Flow<Uri> {
        return newPlaylistRepository.saveImageToPrivateStorage(uri, albumName)
    }
}