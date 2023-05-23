package com.practicum.playlistmaker.player.data

import android.media.MediaPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerStateListener
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.player.domain.model.PlayerState

class TrackPlayerImpl(private val previewUrl: String?) : TrackPlayer {

    private var mediaPlayer = MediaPlayer()
    override var listener: PlayerStateListener? = null

    init {
        listener?.onStateChanged(PlayerState.STATE_DEFAULT)
    }

    override fun preparePlayer() {
        if (previewUrl.isNullOrEmpty()) return

        mediaPlayer.setDataSource(previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
        mediaPlayer.setOnCompletionListener {
            listener?.onStateChanged(PlayerState.STATE_PREPARED)
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        listener?.onStateChanged(PlayerState.STATE_PLAYING)
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        listener?.onStateChanged(PlayerState.STATE_PAUSED)
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer.currentPosition
    }
}