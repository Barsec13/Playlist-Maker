package com.practicum.playlistmaker.media.presentation

import com.practicum.playlistmaker.media.domain.api.MediaInteractor
import com.practicum.playlistmaker.domain.models.PlayerState

class MediaPresenter(
    private var view: MediaView?,
    private var mediaInteractor: MediaInteractor,
    private var mediaRouter: MediaRouter
) {

    init {
        mediaInteractor.subscribeOnPlayer { state ->
            when(state) {
                PlayerState.STATE_PLAYING -> startPlayer()
                PlayerState.STATE_PREPARED -> preparePlayer()
                PlayerState.STATE_PAUSED  -> pausePlayer()
                PlayerState.STATE_DEFAULT -> onScreenDestroyed()
            }
        }
    }

    var playerState = PlayerState.STATE_DEFAULT

    fun loadInfoTrack(){
        view!!.showDataTrack(mediaRouter.getToMedia())
    }

    fun startPreparePlayer(){
        mediaInteractor.preparePlayer()
    }

    fun playbackControl(){

        when(playerState){
            PlayerState.STATE_PLAYING -> mediaInteractor.pausePlayer()
            PlayerState.STATE_PREPARED,
            PlayerState.STATE_PAUSED  -> mediaInteractor.startPlayer()
            PlayerState.STATE_DEFAULT -> defaultPlayer()
        }
    }

    fun defaultPlayer(){
        playerState = PlayerState.STATE_DEFAULT
    }

    fun preparePlayer(){
        view?.preparePlayer()
        playerState = PlayerState.STATE_PREPARED
    }

    fun startPlayer(){
        playerState = PlayerState.STATE_PLAYING
        view?.startPlayer()
    }

    fun pausePlayer(){
        view?.pausePlayer()
        playerState = PlayerState.STATE_PAUSED
    }

    fun onScreenDestroyed() {
        mediaInteractor.unSubscribeOnPlayer()
    }

    fun onViewDestroyed() {
        mediaInteractor.releasePlayer()
        view = null
    }

    fun getCurrentPosition(): String{
        return mediaInteractor.getCurrentPosition()
    }
}