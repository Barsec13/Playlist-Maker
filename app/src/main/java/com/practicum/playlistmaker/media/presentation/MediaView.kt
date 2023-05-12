package com.practicum.playlistmaker.media.presentation

import com.practicum.playlistmaker.media.domain.model.Track

interface MediaView {
    fun showDataTrack(track: Track)
    fun startPlayer()
    fun pausePlayer()
    fun preparePlayer()
}