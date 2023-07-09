package com.practicum.playlistmaker.player.ui.models

interface LikeStateInterface {
    object LikeTrack: LikeStateInterface

    object NotLikeTrack: LikeStateInterface
}