package com.practicum.playlistmaker.new_playlist.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import androidx.core.net.toUri
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import java.io.FileOutputStream

class NewPlaylistsRepositoryImpl(private val context: Context): NewPlaylistsRepository {
    override suspend fun saveImageToPrivateStorage(uri: Uri, albumName: String): Flow<Uri> = flow {
        val filePath = File(
            context
                .getExternalFilesDir(Environment.DIRECTORY_PICTURES),
            "album_Cover_Playlist"
        )
        if (!filePath.exists()) {
            filePath.mkdirs()
        }
        val file = File(filePath, "cover_$albumName.jpg")
        val inputStream = context.contentResolver.openInputStream(uri)
        val outputStream = FileOutputStream(file)

        BitmapFactory
            .decodeStream(inputStream)
            .compress(Bitmap.CompressFormat.JPEG, 30, outputStream)

        emit(file.toUri())
    }
}