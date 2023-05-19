package com.practicum.playlistmaker.search.creator


import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.NetworkClientImpl
import com.practicum.playlistmaker.search.data.network.iTunesSearchAPI
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesClient
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesClientImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.search.ui.activity.HISTORY_TRACKS_SHARED_PREF
import com.practicum.playlistmaker.search.ui.router.SearchNavigationRouter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CreatorSearch {
    private fun getsharedPref(context: Context): SharedPreferences {
        return context.getSharedPreferences(
            HISTORY_TRACKS_SHARED_PREF,
            AppCompatActivity.MODE_PRIVATE
        )
    }

    private fun getSharedPreferencesClient(context: Context): SharedPreferencesClient {
        return SharedPreferencesClientImpl(getsharedPref(context))
    }

    fun getNetworkClient(): NetworkClient {
        return NetworkClientImpl(getRetrofit().create(iTunesSearchAPI::class.java))
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(
            getSharedPreferencesClient(context),
            getNetworkClient(),
        )
    }

    //Базовый URL iTunes Search API
    private val baseURLiTunesSearchAPI = "https://itunes.apple.com"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseURLiTunesSearchAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getSearchNavigationRouter(activity: AppCompatActivity): SearchNavigationRouter {
        return SearchNavigationRouter(activity)
    }
}