package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel {
        MainViewModel()
    }

    viewModel {
        PlayerViewModel(
            //application = get(),
            playerInteractor = get(),
        )
    }

    viewModel {
        SearchViewModel(
            //application = get(),
            searchInteractor = get()
        )
    }

    viewModel {
        SettingsViewModel(
            //application = get(),
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }
}