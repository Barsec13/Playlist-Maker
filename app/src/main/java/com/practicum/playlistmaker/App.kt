package com.practicum.playlistmaker

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatDelegate
import com.practicum.playlistmaker.settings.creator.CreatorSettings

class App : Application() {

    private var darkTheme = false

    override fun onCreate() {
        super.onCreate()

        val settingsInteractor = CreatorSettings.provideSettingsInteractor(this)

        darkTheme = settingsInteractor.getThemeSettings().darkTheme

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
}