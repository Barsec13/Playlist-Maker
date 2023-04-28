package com.practicum.playlistmaker.media.domain.imlp

import com.practicum.playlistmaker.TimeUtils.formatTrackDuraction
import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.media.domain.api.PlayerStateListener
import com.practicum.playlistmaker.media.domain.api.TrackPlayer

class MediaInteractorImpl(private val trackPlayer: TrackPlayer): MediaInteractor {
    override fun preparePlayer() {
        trackPlayer.preparePlayer()
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

    override fun releasePlayer(){
        trackPlayer.releasePlayer()
    }

    override fun getCurrentPosition(): String {
        return formatTrackDuraction(trackPlayer.getCurrentPosition())
    }
}