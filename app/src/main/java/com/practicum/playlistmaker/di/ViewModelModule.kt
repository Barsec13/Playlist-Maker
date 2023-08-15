package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.editplaylist.ui.view_model.EditPlaylistViewModel
import com.practicum.playlistmaker.medialibrary.ui.view_model.FavoriteTrackViewModel
import com.practicum.playlistmaker.medialibrary.ui.view_model.PlaylistsViewModel
import com.practicum.playlistmaker.newplaylist.ui.view_model.NewPlaylistViewModel
import com.practicum.playlistmaker.player.ui.view_model.PlayerViewModel
import com.practicum.playlistmaker.playlist.ui.view_model.PlaylistViewModel
import com.practicum.playlistmaker.search.ui.view_model.SearchViewModel
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {
    viewModel { (trackId: Int) ->
        PlayerViewModel(
            trackId = trackId,
            playerInteractor = get(),
            favoriteTrackInteractor = get(),
            playlistDbInteractor = get()
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

    viewModel {
        FavoriteTrackViewModel(favoriteTrackInteractor = get())
    }

    viewModel {
        PlaylistsViewModel(get())
    }

    viewModel {
        NewPlaylistViewModel(
            playlistDbInteractor = get(),
            newPlaylistInteractor = get()
        )
    }

    viewModel { (playlistId: Int) ->
        PlaylistViewModel(
            playlistInteractor = get(),
            playlistDbInteractor = get(),
            playlistId = playlistId
        )
    }

    viewModel { (playlistId: Int) ->
        EditPlaylistViewModel(
            playlistDbInteractor = get(),
            newPlaylistInteractor = get(),
            playlistId = playlistId
        )
    }
}