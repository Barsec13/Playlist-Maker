package com.practicum.playlistmaker.settings.ui.router

import androidx.appcompat.app.AppCompatActivity

class SettingsNavigationRouter(private val activity: AppCompatActivity) {
    fun backView() {
        activity.finish()
    }
}