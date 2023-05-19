package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}