package com.practicum.playlistmaker.main.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media_library.ui.activity.MediaLibraryActivity
import com.practicum.playlistmaker.search.ui.activity.SearchActivity
import com.practicum.playlistmaker.settings.ui.activity.SettingsActivity

class NavigationRouter(private val activity: AppCompatActivity) {
    fun getSearch() {
        val searchIntent = Intent(activity, SearchActivity::class.java)
        activity.startActivity(searchIntent)
    }

    fun getSettings() {
        val settingsIntent = Intent(activity, SettingsActivity::class.java)
        activity.startActivity(settingsIntent)
    }

    fun getMediaLibrary() {
        val mediaIntent = Intent(activity, MediaLibraryActivity::class.java)
        activity.startActivity(mediaIntent)
    }
}