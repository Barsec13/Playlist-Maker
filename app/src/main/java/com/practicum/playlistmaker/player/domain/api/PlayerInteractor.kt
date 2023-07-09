package com.practicum.playlistmaker.player.domain.api

import com.practicum.playlistmaker.player.domain.model.Track
import kotlinx.coroutines.flow.Flow

interface PlayerInteractor {

    fun preparePlayer(previewUrl: String?)
    fun startPlayer()
    fun pausePlayer()
    fun subscribeOnPlayer(listener: PlayerStateListener)
    fun unSubscribeOnPlayer()
    fun releasePlayer()
    fun getCurrentPosition(): String
    fun getTrack(trackId: Int): Track?
    fun checkFavorite(sendTrack: Track)
    suspend fun getTrackFromDataBase(trackId: Int): Flow<Track>
}