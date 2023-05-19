package com.practicum.playlistmaker.settings.data.impl

import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.sharedpreferences.SharedPrefSettingsClient
import com.practicum.playlistmaker.settings.domain.model.ThemeSettings

class SettingsRepositoryImpl(private val sharedPrefSettingsClient: SharedPrefSettingsClient) :
    SettingsRepository {
    override fun getThemeSettings(): ThemeSettings {
        return sharedPrefSettingsClient.getTheme()
    }

    override fun updateThemeSetting(settings: ThemeSettings) {
        sharedPrefSettingsClient.saveTheme(settings)
    }
}