package com.practicum.playlistmaker.media.domain.api

interface MediaInteractor {

    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun subscribeOnPlayer(listener: PlayerStateListener)
    fun unSubscribeOnPlayer()
    fun releasePlayer()
    fun getCurrentPosition(): String
}