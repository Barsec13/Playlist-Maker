package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.player.data.impl.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import org.koin.dsl.module

val repositoryModule = module {
    factory <TrackPlayer> {
        TrackPlayerImpl()
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(sharedPrefSettingsClient = get()) }
}