package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.media.domain.model.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}