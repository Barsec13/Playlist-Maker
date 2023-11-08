package com.practicum.playlistmaker.newplaylist.domain.impl

import android.net.Uri
import com.practicum.playlistmaker.newplaylist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.newplaylist.domain.api.NewPlaylistsRepository
import kotlinx.coroutines.flow.Flow

class NewPlaylistInteractorImpl(private val newPlaylistsRepository: NewPlaylistsRepository): NewPlaylistInteractor {
    override suspend fun saveImageToPrivateStorage(uri: Uri, albumName: String): Flow<Uri> {
        return newPlaylistsRepository.saveImageToPrivateStorage(uri, albumName)
    }
}