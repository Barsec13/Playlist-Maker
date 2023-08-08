package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.db.data.converter.PlaylistDbConverter
import com.practicum.playlistmaker.db.data.converter.TrackDbConverter
import com.practicum.playlistmaker.db.data.impl.FavoriteTrackRepositoryImpl
import com.practicum.playlistmaker.db.data.impl.PlayListRepositoryImpl
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackRepository
import com.practicum.playlistmaker.db.domain.api.PlayListRepository
import com.practicum.playlistmaker.new_playlist.data.repository.NewPlaylistRepositoryImpl
import com.practicum.playlistmaker.new_playlist.domain.api.NewPlaylistRepository
import com.practicum.playlistmaker.player.data.impl.TrackPlayerImpl
import com.practicum.playlistmaker.player.data.repository.PlayerRepositoryImpl
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
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

    single<PlayListRepository> {
        PlayListRepositoryImpl(appDataBase = get(), playlistDbConverter = get(), gson = get())
    }

    single<NewPlaylistRepository> {
        NewPlaylistRepositoryImpl(context = get())
    }
}