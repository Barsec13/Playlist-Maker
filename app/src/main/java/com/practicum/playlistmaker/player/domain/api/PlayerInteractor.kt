package com.practicum.playlistmaker.player.domain.api

interface PlayerInteractor {

    fun preparePlayer()
    fun startPlayer()
    fun pausePlayer()
    fun subscribeOnPlayer(listener: PlayerStateListener)
    fun unSubscribeOnPlayer()
    fun releasePlayer()
    fun getCurrentPosition(): String
}