package com.practicum.playlistmaker.media.presentation

import com.practicum.playlistmaker.domain.models.Track

interface MediaView {
    fun showDataTrack(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer()
}