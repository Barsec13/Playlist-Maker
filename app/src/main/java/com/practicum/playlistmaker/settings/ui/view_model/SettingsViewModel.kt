package com.practicum.playlistmaker.settings.ui.view_model

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.settings.creator.CreatorSettings
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.sharing.creator.CreatorSharing
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor

class SettingsViewModel(
    private val application: Application,
    private val sharingInteractor: SharingInteractor,
    private val settingsInteractor: SettingsInteractor,
) : AndroidViewModel(application) {
    companion object {
        fun getSettingsViewModelFactory(): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                SettingsViewModel(
                    application = application,
                    sharingInteractor = CreatorSharing.provideSharingInteractor(application),
                    settingsInteractor = CreatorSettings.provideSettingsInteractor(application)
                )
            }
        }
    }

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