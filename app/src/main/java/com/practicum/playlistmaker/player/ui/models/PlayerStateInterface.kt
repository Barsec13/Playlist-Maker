package com.practicum.playlistmaker.player.ui.models

sealed interface PlayerStateInterface {
    object Prepare : PlayerStateInterface
    object Play : PlayerStateInterface
    object Pause : PlayerStateInterface
}