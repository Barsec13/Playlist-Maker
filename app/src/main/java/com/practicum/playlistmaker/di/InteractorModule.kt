package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.db.domain.api.FavoriteTrackInteractor
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.db.domain.impl.FavoriteTrackInteractorImpl
import com.practicum.playlistmaker.db.domain.impl.PlaylistDbInteractorImpl
import com.practicum.playlistmaker.newplaylist.domain.api.NewPlaylistInteractor
import com.practicum.playlistmaker.newplaylist.domain.impl.NewPlaylistInteractorImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.domain.impl.PlaylistInteractorImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.sharing.domain.api.SharingInteractor
import com.practicum.playlistmaker.sharing.domain.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory<PlayerInteractor> {
        PlayerInteractorImpl(
            trackPlayer = get(),
            sharedPreferencesPlayerClientImpl = get(),
            playerRepository = get()
        )
    }

    single<SearchInteractor> {
        SearchInteractorImpl(
            sharedPreferencesSearchClient = get(),
            searchRepository = get()
        )
    }

    single<SettingsInteractor> {
        SettingsInteractorImpl(settingsRepository = get())
    }

    single<SharingInteractor> { SharingInteractorImpl(externalNavigator = get()) }

    single<FavoriteTrackInteractor> {
        FavoriteTrackInteractorImpl(favoriteTrackRepository = get())
    }

    single<PlaylistDbInteractor> {
        PlaylistDbInteractorImpl(playListDbRepository = get())
    }

    single<NewPlaylistInteractor> {
        NewPlaylistInteractorImpl(newPlaylistsRepository = get())
    }

    single<PlaylistInteractor> {
        PlaylistInteractorImpl(
            playlistRepository = get(),
            playListDbRepository = get(),
            externalNavigatorPlaylist = get()
        )
    }
}