package com.practicum.playlistmaker.media.creator

import com.practicum.playlistmaker.media.data.TrackPlayerImpl
import com.practicum.playlistmaker.media.domain.impl.MediaInteractorImpl
import com.practicum.playlistmaker.media.presentation.MediaPresenter
import com.practicum.playlistmaker.media.presentation.MediaRouter
import com.practicum.playlistmaker.media.presentation.MediaView

object CreatorMedia {
    fun provideMediaPresenter(mediaRouter: MediaRouter, view: MediaView): MediaPresenter {
        val previewUrl: String = mediaRouter.getToMedia().previewUrl
        return MediaPresenter(
            view = view,
            mediaInteractor = MediaInteractorImpl(trackPlayer = TrackPlayerImpl(previewUrl)),
            mediaRouter = mediaRouter
        )
    }
}