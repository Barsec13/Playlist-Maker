package com.practicum.playlistmaker.media.domain.api

import com.practicum.playlistmaker.domain.models.PlayerState

fun interface PlayerStateListener {
    fun onStateChanged(state: PlayerState)
}