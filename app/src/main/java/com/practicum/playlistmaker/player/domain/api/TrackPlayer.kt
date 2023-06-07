package com.practicum.playlistmaker.player.domain.api

interface TrackPlayer {
    var listener: PlayerStateListener?
    fun preparePlayer(previewUrl: String?)
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}