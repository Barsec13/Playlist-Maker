package com.practicum.playlistmaker.player.ui.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface

class PlayerViewModel(private val playerInteractor: PlayerInteractor) : ViewModel() {

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
        private val SEARCH_REQUEST_TOKEN = Any()
        private const val SEARCH_DEBOUNCE_DELAY = 1000L
    }

    private val handler = Handler(Looper.getMainLooper())
    var playerState = PlayerState.STATE_DEFAULT

    private val playerStateLiveData = MutableLiveData<PlayerStateInterface>()
    private val timerLiveData = MutableLiveData<String>()
    fun observePlayerState(): LiveData<PlayerStateInterface> = playerStateLiveData
    fun observerTimerState(): LiveData<String> = timerLiveData

    override fun onCleared() {
        pausePlayer()
        onViewDestroyed()
        onScreenDestroyed()
        handler.removeCallbacksAndMessages(null)
    }

    fun playbackControl() {

        when (playerState) {
            PlayerState.STATE_PLAYING -> {
                handler.removeCallbacksAndMessages(null)
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

        handler.removeCallbacksAndMessages(null)

        handler.postDelayed(
            object : Runnable {
                override fun run() {
                    timerLiveData.value = getCurrentPosition()
                    handler.postDelayed(this, SEARCH_DEBOUNCE_DELAY)
                }
            },
            SEARCH_REQUEST_TOKEN, SEARCH_DEBOUNCE_DELAY
        )
    }

    private fun pausePlayer() {
        playerState = PlayerState.STATE_PAUSED
        handler.removeCallbacksAndMessages(null)
        playerStateLiveData.postValue(PlayerStateInterface.Pause)
    }


    fun startPreparePlayer(previewUrl: String?) {
        playerInteractor.preparePlayer(previewUrl)
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(PlayerStateInterface.Prepare)
    }

    private fun defaultPlayer() {
        handler.removeCallbacksAndMessages(null)
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun preparePlayer() {
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(PlayerStateInterface.Prepare)
        handler.removeCallbacksAndMessages(null)
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
}