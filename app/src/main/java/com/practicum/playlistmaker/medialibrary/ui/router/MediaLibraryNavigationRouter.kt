package com.practicum.playlistmaker.medialibrary.ui.router

import androidx.appcompat.app.AppCompatActivity

class MediaLibraryNavigationRouter(private val activity: AppCompatActivity) {
    fun backView() {
        activity.finish()
    }
}