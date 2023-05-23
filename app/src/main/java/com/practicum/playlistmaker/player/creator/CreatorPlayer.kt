package com.practicum.playlistmaker.player.creator

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.player.data.TrackPlayerImpl
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.player.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.player.ui.router.PlayerRouter

object CreatorPlayer {
    fun provideMediaInteractor(previewUrl: String?): PlayerInteractor {
        return PlayerInteractorImpl(
            trackPlayer = getTrackPlayer(
                previewUrl = previewUrl
            )
        )
    }

    fun getTrackPlayer(previewUrl: String?): TrackPlayer {
        return TrackPlayerImpl(previewUrl)
    }

    fun getMediaRouter(activity: AppCompatActivity): PlayerRouter {
        return PlayerRouter(activity = activity)
    }
}