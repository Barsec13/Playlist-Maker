package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.TimeUtils.formatTrackDuraction
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerStateListener
import com.practicum.playlistmaker.player.domain.api.TrackPlayer

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer) : PlayerInteractor {
    override fun preparePlayer(previewUrl: String?) {
        trackPlayer.preparePlayer(previewUrl)
    }

    override fun startPlayer() {
        trackPlayer.startPlayer()
    }

    override fun pausePlayer() {
        trackPlayer.pausePlayer()
    }

    override fun subscribeOnPlayer(listener: PlayerStateListener) {
        trackPlayer.listener = listener
    }

    override fun unSubscribeOnPlayer() {
        trackPlayer.listener = null
    }

    override fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    override fun getCurrentPosition(): String {
        return formatTrackDuraction(trackPlayer.getCurrentPosition())
    }
}