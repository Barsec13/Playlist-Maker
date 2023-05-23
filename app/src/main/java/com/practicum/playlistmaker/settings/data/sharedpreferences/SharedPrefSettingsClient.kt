package com.practicum.playlistmaker.settings.data.sharedpreferences

import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

interface SharedPrefSettingsClient {
    fun saveTheme(themeSettings: ThemeSettings)
    fun getTheme(): ThemeSettings
}