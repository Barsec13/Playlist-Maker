package com.practicum.playlistmaker.settings.ui.view_model

import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : ViewModel() {

    private var darkTheme = false
    private val themeSwitcherstateLiveData = MutableLiveData(darkTheme)

    init {
        darkTheme = settingsInteractor.getThemeSettings().darkTheme
        themeSwitcherstateLiveData.value = darkTheme
    }

    fun observeThemeSwitcherChecked(): LiveData<Boolean> = themeSwitcherstateLiveData

    fun clickShareApp() {
        sharingInteractor.shareApp()
    }

    fun clickApplySupport() {
        sharingInteractor.openSupport()
    }

    fun clickViewUserAgreement() {
        sharingInteractor.openTerms()
    }

    fun clickThemeSwitcher(isChecked: Boolean) {
        themeSwitcherstateLiveData.value = isChecked
        settingsInteractor.updateThemeSetting(isChecked)
        switchTheme(isChecked)
    }

    private fun switchTheme(darkThemeEnabled: Boolean) {
        darkTheme = darkThemeEnabled

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}