package com.practicum.playlistmaker.settings.data.sharedpreferences

import android.content.SharedPreferences
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SharedPrefSettingsClientImpl(private val sharedPref: SharedPreferences): SharedPrefSettingsClient {
    override fun saveTheme(themeSettings: ThemeSettings){
        sharedPref.edit().putBoolean(DARK_THEME_KEY, themeSettings.darkTheme).apply()
    }

    override fun getTheme(): ThemeSettings{
        return ThemeSettings(darkTheme = sharedPref.getBoolean(DARK_THEME_KEY, false))
    }

    companion object{
        const val DARK_THEME_KEY = "key_for_dark_theme"
    }
}