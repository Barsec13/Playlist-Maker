package com.practicum.playlistmaker.di

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import com.practicum.playlistmaker.db.data.AppDataBase
import com.practicum.playlistmaker.player.data.sharedpreferences.SharedPreferencesPlayerClient
import com.practicum.playlistmaker.player.data.sharedpreferences.SharedPreferencesPlayerClientImpl
import com.practicum.playlistmaker.search.data.network.CheckConnection
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.NetworkClientImpl
import com.practicum.playlistmaker.search.data.network.iTunesSearchAPI
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesSearchClient
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesSearchClientImpl
import com.practicum.playlistmaker.settings.data.sharedpreferences.SharedPrefSettingsClient
import com.practicum.playlistmaker.settings.data.sharedpreferences.SharedPrefSettingsClientImpl
import com.practicum.playlistmaker.sharing.data.ExternalNavigator
import com.practicum.playlistmaker.sharing.data.impl.ExternalNavigatorImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {
    single<iTunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build().create(iTunesSearchAPI::class.java)
    }

    single<NetworkClient> {
        NetworkClientImpl(
            api = get(),
            checkConnection = get())
    }

    factory { Gson() }

    single {
        androidContext().getSharedPreferences("tracks_shared_pref", Context.MODE_PRIVATE)
    }

    single<SharedPreferencesSearchClient> {
        SharedPreferencesSearchClientImpl(
            sharedPref = get(),
            gson = get(),
            appDataBase = get()
        )
    }

    single<SharedPrefSettingsClient> {
        SharedPrefSettingsClientImpl(sharedPref = get())
    }

    single<ExternalNavigator> { ExternalNavigatorImpl(androidContext()) }

    single<SharedPreferencesPlayerClient> {
        SharedPreferencesPlayerClientImpl(
            sharedPref = get(),
            gson = get(),
        )
    }

    single<CheckConnection> {
        CheckConnection(context = androidContext())
    }

    single {
        Room.databaseBuilder(androidContext(), AppDataBase::class.java, "database.db").build()
    }
}