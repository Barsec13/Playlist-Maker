package com.practicum.playlistmaker

import android.app.Activity
import android.app.Application
import android.app.UiModeManager.MODE_NIGHT_YES
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.app.AppCompatDelegate.MODE_NIGHT_YES

const val PLAY_LIST_MAKER_SHARED_PREFERENCES = "play_list_maker_shared_preferences"
const val DARK_THEME_KEY = "key_for_dark_theme"

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE)
        darkTheme = sharedPrefs.getBoolean(DARK_THEME_KEY, false)

        AppCompatDelegate.setDefaultNightMode(
            if (darkTheme || isDarkMode(applicationContext as App)) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun isDarkMode(context: Context): Boolean {
        val darkModeFlag = context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return darkModeFlag == Configuration.UI_MODE_NIGHT_YES
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    fun saveTheme(darkThemeEnabled: Boolean){
        getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE).edit()
            .putBoolean(DARK_THEME_KEY, darkThemeEnabled)
            .apply()
    }
}