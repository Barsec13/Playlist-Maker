package com.practicum.playlistmaker.media.domain.api

interface TrackPlayer {
    var listener: PlayerStateListener?
    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun releasePlayer()
    fun getCurrentPosition(): Int
}