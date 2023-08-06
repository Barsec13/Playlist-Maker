package com.practicum.playlistmaker.player.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackInteractor
import com.practicum.playlistmaker.db.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.media_library.ui.models.PlaylistStateInterface
import com.practicum.playlistmaker.new_playlist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.api.PlayerInteractor
import com.practicum.playlistmaker.player.domain.model.PlayerState
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.models.LikeStateInterface
import com.practicum.playlistmaker.player.ui.models.PlayerStateInterface
import com.practicum.playlistmaker.player.ui.models.TrackInPlaylistStateInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackId: Int,
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
    private val playlistInteractor: PlaylistInteractor,
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
        getInfoTrack()
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
    private val playlistStateLiveData = MutableLiveData<PlaylistStateInterface>()
    private val trackInPlaylistState = MutableLiveData<TrackInPlaylistStateInterface>()
    fun observePlayerState(): LiveData<PlayerStateInterface> = playerStateLiveData
    fun observerTimerState(): LiveData<String> = timerLiveData
    fun observeTrackState(): LiveData<Track> = trackStateLiveData
    fun observeIsFavoriteState(): LiveData<LikeStateInterface> = isFavoriteStateLiveData

    fun observePlaylistState(): LiveData<PlaylistStateInterface> = playlistStateLiveData
    fun observeTrackInPlaylistState(): LiveData<TrackInPlaylistStateInterface> =
        trackInPlaylistState

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
        if (playerState == PlayerState.STATE_PREPARED) return
        playerState = PlayerState.STATE_PAUSED
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


    private fun startPreparePlayer(previewUrl: String?) {
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
        timerJob?.cancel()
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

    private fun getInfoTrack() {
        sendTrack = playerInteractor.getTrack(trackId)

        if (sendTrack == null) getTrackFromDataBase(trackId)
        else {
            viewModelScope.launch {
                playerInteractor.checkFavorite(sendTrack!!.trackId).collect {
                    sendTrack!!.isFavorite = it
                    trackState(sendTrack)
                }
            }
        }
    }

    private fun trackState(track: Track?) {
        this.sendTrack = track
        if (sendTrack == null) return

        checkFavorite(sendTrack)
        startPreparePlayer(sendTrack!!.previewUrl)
        preparePlayer()
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
                sendTrack!!.isFavorite = false
                favoriteTrackInteractor.deleteTrackOnFavorite(sendTrack!!)
                isFavoriteStateLiveData.postValue(LikeStateInterface.NotLikeTrack)
            } else {
                sendTrack!!.isFavorite = true
                favoriteTrackInteractor.insertFavoriteTrack(sendTrack!!)
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

    fun showPlaylist() {
        viewModelScope.launch {
            playlistInteractor.getPlaylists().collect() { playlists ->
                if (playlists.isEmpty()) playlistStateLiveData.postValue(PlaylistStateInterface.PlaylistsIsEmpty)
                else playlistStateLiveData.postValue(PlaylistStateInterface.Playlists(playlists))
            }
        }
    }

    fun onPlaylistClick(tracksId: List<Int>, playlist: Playlist) {
        if (sendTrack!!.trackId in tracksId) {
            trackInPlaylistState.postValue(TrackInPlaylistStateInterface.TrackOnPlaylist)
        } else {
            viewModelScope.launch {
                playlistInteractor.insertTrackInPlaylist(
                    sendTrack!!,
                    playlist,
                    tracksId
                )
            }
            trackInPlaylistState.postValue(TrackInPlaylistStateInterface.TrackAddToPlaylist)
        }
    }
}