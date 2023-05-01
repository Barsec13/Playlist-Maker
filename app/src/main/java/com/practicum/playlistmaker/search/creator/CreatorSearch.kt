package com.practicum.playlistmaker.search.creator


import android.content.SharedPreferences
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.media.data.TrackPlayerImpl
import com.practicum.playlistmaker.media.domain.impl.MediaInteractorImpl
import com.practicum.playlistmaker.media.presentation.MediaPresenter
import com.practicum.playlistmaker.media.presentation.MediaRouter
import com.practicum.playlistmaker.media.presentation.MediaView
import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.NetworkClientImpl
import com.practicum.playlistmaker.search.data.network.iTunesSearchAPI
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesClient
import com.practicum.playlistmaker.search.data.sharedpreferences.SharedPreferencesClientImpl
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.impl.SearchInteractorImpl
import com.practicum.playlistmaker.search.presentation.SearchPresenter
import com.practicum.playlistmaker.search.presentation.SearchRouter
import com.practicum.playlistmaker.search.presentation.SearchViewActivity
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object CreatorSearch {
    fun provideSearchPresenter(
        viewSearch: SearchViewActivity,
        view: AppCompatActivity,
        sharedPreferences: SharedPreferences
//        searchInteractor: SearchInteractor,
//        searchRouter: SearchRouter,
    ): SearchPresenter {
        return SearchPresenter(
            view = viewSearch,
            searchInteractor = getSearchInteractor(sharedPreferences),
            searchRouter = getSearchRouter(view)
        )
    }

    private fun getSharedPreferences(sharedPreferences: SharedPreferences): SharedPreferencesClient{
        return SharedPreferencesClientImpl(sharedPref = sharedPreferences)
    }

    private fun getSearchInteractor(sharedPreferences: SharedPreferences): SearchInteractor{
        return SearchInteractorImpl(getSharedPreferences(sharedPreferences = sharedPreferences), getNetworkClient())
    }

    //Базовый URL iTunes Search API
    private val baseURLiTunesSearchAPI = "https://itunes.apple.com"

    private fun getRetrofit(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(baseURLiTunesSearchAPI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getNetworkClient(): NetworkClient{
        return NetworkClientImpl(getRetrofit().create(iTunesSearchAPI::class.java))
    }

    fun getSearchRouter(view: AppCompatActivity): SearchRouter{
        return SearchRouter(view)
    }
}