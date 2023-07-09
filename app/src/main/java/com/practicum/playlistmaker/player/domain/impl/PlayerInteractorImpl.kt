package com.practicum.playlistmaker.player.domain.impl

import com.practicum.playlistmaker.player.data.sharedpreferences.SharedPreferencesPlayerClient
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerRepository
import com.practicum.playlistmaker.player.domain.api.PlayerStateListener
import com.practicum.playlistmaker.player.domain.api.TrackPlayer
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.util.TimeUtils.formatTrackDuraction
import kotlinx.coroutines.flow.Flow

class PlayerInteractorImpl(
    private val trackPlayer: TrackPlayer,
    private val sharedPreferencesPlayerClientImpl: SharedPreferencesPlayerClient,
    private val playerRepository: PlayerRepository
) : PlayerInteractor {
    override fun preparePlayer(previewUrl: String?) {
        trackPlayer.preparePlayer(previewUrl)
    }

    override fun startPlayer() {
        trackPlayer.startPlayer()
    }

    override fun pausePlayer() {
        trackPlayer.pausePlayer()
    }

    override fun subscribeOnPlayer(listener: PlayerStateListener) {
        trackPlayer.listener = listener
    }

    override fun unSubscribeOnPlayer() {
        trackPlayer.listener = null
    }

    override fun releasePlayer() {
        trackPlayer.releasePlayer()
    }

    override fun getCurrentPosition(): String {
        return formatTrackDuraction(trackPlayer.getCurrentPosition())
    }

    override fun getTrack(trackId: Int): Track? {
        return sharedPreferencesPlayerClientImpl.tracksHistoryFromJson().find { it.trackId == trackId }
    }

    override suspend fun getTrackFromDataBase(trackId: Int): Flow<Track> {
        return playerRepository.getFavoriteTrackById(trackId)
    }

    override fun checkFavorite(sendTrack: Track) {
        return
    }
}