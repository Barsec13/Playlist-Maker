package com.practicum.playlistmaker.settings.creator

import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.settings.data.SettingsRepository
import com.practicum.playlistmaker.settings.data.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.settings.data.sharedpreferences.SharedPrefSettingsClient
import com.practicum.playlistmaker.settings.data.sharedpreferences.SharedPrefSettingsClientImpl
import com.practicum.playlistmaker.settings.domain.SettingsInteractor
import com.practicum.playlistmaker.settings.domain.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.settings.ui.router.SettingsNavigationRouter

object CreatorSettings {
    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(settingsRepository = provideSettingsRepository(context))
    }

    fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(sharedPrefSettingsClient = getSharedPrefSettingsClient(context))
    }

    private fun getSharedPrefSettingsClient(context: Context): SharedPrefSettingsClient {
        return SharedPrefSettingsClientImpl(getSharedPref(context))
    }

    private fun getSharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE)
    }

    private const val PLAY_LIST_MAKER_SHARED_PREFERENCES = "play_list_maker_shared_preferences"

    fun getSettingsNavigationRouter(activity: AppCompatActivity): SettingsNavigationRouter {
        return SettingsNavigationRouter(activity = activity)
    }
}