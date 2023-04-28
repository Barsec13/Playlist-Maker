package com.practicum.playlistmaker.creator


import com.practicum.playlistmaker.media.data.TrackPlayerImpl
import com.practicum.playlistmaker.media.domain.imlp.MediaInteractorImpl
import com.practicum.playlistmaker.media.presentation.MediaPresenter
import com.practicum.playlistmaker.media.presentation.MediaRouter
import com.practicum.playlistmaker.media.presentation.MediaView
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.presentation.SearchPresenter
import com.practicum.playlistmaker.search.presentation.SearchRouter
import com.practicum.playlistmaker.search.presentation.SearchViewActivity

object Creator {

    fun provideSearchPresenter(
        view: SearchViewActivity,
        searchInteractor: SearchInteractor,
        searchRouter: SearchRouter,
    ): SearchPresenter {
        return SearchPresenter(
            view = view,
            searchInteractor = searchInteractor,
            searchRouter = searchRouter
        )
    }

    fun provideMediaPresenter(mediaRouter: MediaRouter, view: MediaView): MediaPresenter{
        val previewUrl: String = mediaRouter.getToMedia().previewUrl
        return MediaPresenter(
            view = view,
            mediaInteractor = MediaInteractorImpl(trackPlayer = TrackPlayerImpl(previewUrl)),
            mediaRouter = mediaRouter
            )
    }
}