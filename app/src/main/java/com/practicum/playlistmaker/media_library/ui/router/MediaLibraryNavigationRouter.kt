package com.practicum.playlistmaker.media_library.ui.router

import androidx.appcompat.app.AppCompatActivity

class MediaLibraryNavigationRouter(private val activity: AppCompatActivity) {
    fun backView() {
        activity.finish()
    }
}