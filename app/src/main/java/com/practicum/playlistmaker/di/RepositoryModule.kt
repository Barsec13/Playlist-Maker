package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.db.data.converter.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.impl.FavoriteTrackRepositoryImpl
import com.practicum.playlistmaker.db.data.impl.PlayListDbRepositoryImpl
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackRepository
import com.practicum.playlistmaker.db.domain.api.PlayListDbRepository
import com.practicum.playlistmaker.newplaylist.data.repository.NewPlaylistsRepositoryImpl
import com.practicum.playlistmaker.newplaylist.domain.api.NewPlaylistsRepository
import com.practicum.playlistmaker.player.data.impl.TrackPlayerImpl
import com.practicum.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.playlist.data.repository.ExternalNavigatorPlaylistImpl
import com.practicum.playlistmaker.playlist.data.repository.PlaylistRepositoryImpl
import com.practicum.playlistmaker.playlist.domain.api.ExternalNavigatorPlaylist
import com.practicum.playlistmaker.playlist.domain.api.PlaylistRepository
import com.practicum.playlistmaker.search.data.repository.SearchRepositoryImpl
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory <TrackPlayer> {
        TrackPlayerImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPrefSettingsClient = get()) }

    single<SearchRepository> {
        SearchRepositoryImpl(networkClient = get(), appDataBase = get())
    }

    factory { TrackDbConverter() }

    single<FavoriteTrackRepository>{
        FavoriteTrackRepositoryImpl(appDataBase = get(), trackDbConverter = get())
    }

    single<PlayerRepository>{
        PlayerRepositoryImpl(appDataBase = get(), trackDbConverter = get())
    }

    factory { PlaylistDbConverter() }

    single<PlayListDbRepository> {
        PlayListDbRepositoryImpl(appDataBase = get(), playlistDbConverter = get(), gson = get())
    }

    single<NewPlaylistsRepository> {
        NewPlaylistsRepositoryImpl(context = get())
    }

    single<PlaylistRepository> {
        PlaylistRepositoryImpl(gson = get())
    }

    single<ExternalNavigatorPlaylist> {
        ExternalNavigatorPlaylistImpl(context = get())
    }
}