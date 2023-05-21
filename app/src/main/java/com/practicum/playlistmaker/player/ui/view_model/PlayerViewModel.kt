package com.practicum.playlistmaker.player.ui.view_model

import android.app.Application
import android.os.Handler
import android.os.Looper
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.practicum.playlistmaker.player.creator.CreatorPlayer
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface

class PlayerViewModel(
    private var application: Application,
    private var playerInteractor: PlayerInteractor,
) : AndroidViewModel(application) {

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
        private val SEARCH_DEBOUNCE_DELAY = 300L
        fun getViewModelFactory(previewUrl: String?): ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application =
                    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as Application
                PlayerViewModel(
                    application = application,
                    playerInteractor = CreatorPlayer.provideMediaInteractor(previewUrl = previewUrl)
                )
            }
        }
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
            PlayerState.STATE_PLAYING -> playerInteractor.pausePlayer()
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

        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        handler.postDelayed(object : Runnable {
            override fun run() {
                timerLiveData.value = getCurrentPosition()
                handler.postDelayed(this, SEARCH_DEBOUNCE_DELAY)
            }
        }, SEARCH_REQUEST_TOKEN,SEARCH_DEBOUNCE_DELAY)
    }

    private fun pausePlayer() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerState = PlayerState.STATE_PAUSED
        playerStateLiveData.postValue(PlayerStateInterface.Pause)
    }


    fun startPreparePlayer() {
        playerInteractor.preparePlayer()
        playerState = PlayerState.STATE_PREPARED
        playerStateLiveData.postValue(PlayerStateInterface.Prepare)
    }

    private fun defaultPlayer() {
        handler.removeCallbacksAndMessages(SEARCH_REQUEST_TOKEN)
        playerState = PlayerState.STATE_DEFAULT
    }

    private fun preparePlayer() {
        playerState = PlayerState.STATE_PREPARED
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