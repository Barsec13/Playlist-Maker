package com.practicum.playlistmaker.media_library.creator

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media_library.ui.router.MediaLibraryNavigationRouter

object CreatorMediaLibrary {
    fun getMediaLibraryNavigationRouter(activity: AppCompatActivity): MediaLibraryNavigationRouter {
        return MediaLibraryNavigationRouter(activity = activity)
    }
}