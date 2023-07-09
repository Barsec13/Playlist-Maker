package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.media_library.ui.view_model.FavoriteTrackViewModel
import com.practicum.playlistmaker.media_library.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (trackId: Int) ->
        PlayerViewModel(
            trackId = trackId,
            playerInteractor = get(),
            favoriteTrackInteractor = get()
        )
    }

    viewModel {
        SearchViewModel(
            searchInteractor = get()
        )
    }

    viewModel {
        SettingsViewModel(
            sharingInteractor = get(),
            settingsInteractor = get()
        )
    }

    viewModel{
        FavoriteTrackViewModel(favoriteTrackInteractor = get())
    }

    viewModel{
        PlaylistViewModel()
    }
}