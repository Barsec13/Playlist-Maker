package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackInteractor
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.models.LikeStateInterface
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackId: Int,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    init {
        playerInteractor.subscribeOnPlayer { state ->
            when (state) {
                PlayerState.STATE_PLAYING -> startPlayer()
                PlayerState.STATE_PREPARED -> preparePlayer()
                PlayerState.STATE_PAUSED -> pausePlayer()
                PlayerState.STATE_DEFAULT -> onScreenDestroyed()
            }
        }
    }

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 300L
    }

    private var timerJob: Job? = null
    var playerState = PlayerState.STATE_DEFAULT
    private var sendTrack: Track? = null

    private val playerStateLiveData = MutableLiveData<PlayerStateInterface>()
    private val timerLiveData = MutableLiveData<String>()
    private val trackStateLiveData = MutableLiveData<Track>()
    private val isFavoriteStateLiveData = MutableLiveData<LikeStateInterface>()
    fun observePlayerState(): LiveData<PlayerStateInterface> = playerStateLiveData
    fun observerTimerState(): LiveData<String> = timerLiveData
    fun observeTrackState(): LiveData<Track> = trackStateLiveData
    fun observeIsFavoriteState(): LiveData<LikeStateInterface> = isFavoriteStateLiveData

    override fun onCleared() {
        pausePlayer()
        onViewDestroyed()
        onScreenDestroyed()
    }

    fun playbackControl() {

        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                playerInteractor.pausePlayer()
            }

            PlayerState.STATE_PREPARED,
            PlayerState.STATE_PAUSED,
            -> playerInteractor.startPlayer()

            PlayerState.STATE_DEFAULT -> defaultPlayer()
        }
    }

    fun activityPause() {
        playerInteractor.pausePlayer()
    }

    private fun startPlayer() {
        playerState = PlayerState.STATE_PLAYING
        playerStateLiveData.postValue(PlayerStateInterface.Play)
        startTimer()
    }

    private fun pausePlayer() {
        playerState = PlayerState.STATE_PAUSED
        playerStateLiveData.postValue(PlayerStateInterface.Pause)
        timerJob?.cancel()
    }


    fun startPreparePlayer(previewUrl: String?) {
        playerInteractor.preparePlayer(previewUrl)
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(PlayerStateInterface.Prepare)
    }

    private fun defaultPlayer() {
        pausePlayer()
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun preparePlayer() {
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(PlayerStateInterface.Prepare)
    }

    private fun onScreenDestroyed() {
        playerInteractor.unSubscribeOnPlayer()
    }

    private fun onViewDestroyed() {
        playerInteractor.releasePlayer()
    }

    private fun getCurrentPosition(): String {
        return playerInteractor.getCurrentPosition()
    }

    fun getInfoTrack() {
        sendTrack = playerInteractor.getTrack(trackId)
        if (sendTrack == null) getTrackFromDataBase(trackId)
        else trackState(sendTrack!!)
    }

    private fun trackState(track: Track?) {
        this.sendTrack = track
        if (sendTrack == null) return
        checkFavorite(sendTrack)
        trackStateLiveData.postValue(sendTrack!!)
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (playerState == PlayerState.STATE_PLAYING) {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                timerLiveData.value = getCurrentPosition()
            }
        }
    }

    fun onFavoriteClicked() {
        viewModelScope.launch {
            if (sendTrack!!.isFavorite) {
                favoriteTrackInteractor.deleteTrackOnFavorite(sendTrack!!)
                sendTrack!!.isFavorite = false
                isFavoriteStateLiveData.postValue(LikeStateInterface.NotLikeTrack)
            } else {
                favoriteTrackInteractor.insertFavoriteTrack(sendTrack!!)
                sendTrack!!.isFavorite = true
                isFavoriteStateLiveData.postValue(LikeStateInterface.LikeTrack)
            }
        }
    }

    fun checkFavorite(sendTrack: Track?) {
        if (sendTrack!!.isFavorite) isFavoriteStateLiveData.postValue(LikeStateInterface.LikeTrack)
        else isFavoriteStateLiveData.postValue(LikeStateInterface.NotLikeTrack)
    }

    private fun getTrackFromDataBase(trackId: Int) {
        viewModelScope.launch {
            playerInteractor.getTrackFromDataBase(trackId).collect() {
                it.isFavorite = true
                trackState(it)
            }
        }
    }
}