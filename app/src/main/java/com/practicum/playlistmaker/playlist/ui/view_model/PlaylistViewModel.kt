package com.practicum.playlistmaker.playlist.ui.view_model

import android.icu.text.SimpleDateFormat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.PlaylistDbInteractor
import com.practicum.playlistmaker.newplaylist.domain.model.Playlist
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.playlist.domain.api.PlaylistInteractor
import com.practicum.playlistmaker.playlist.ui.models.SharePlaylistStateInterface
import com.practicum.playlistmaker.util.TimeUtils
import kotlinx.coroutines.launch
import java.util.Locale

class PlaylistViewModel(
    private val playlistInteractor: PlaylistInteractor,
    private val playlistDbInteractor: PlaylistDbInteractor,
    private val playlistId: Int,
) : ViewModel() {

    var playlist: Playlist? = null
    private var trackInPlaylist = arrayListOf<Track>() as List<Track>


    private val playlistStateLiveData = MutableLiveData<Playlist>()
    private val tracksInPlaylistStateLiveData = MutableLiveData<List<Track>>()
    private val allTimeTracksStateLiveData = MutableLiveData<String>()
    private val quantityTracksStateLiveData = MutableLiveData<Int>()
    private val sharePlaylistStateLiveData = MutableLiveData<SharePlaylistStateInterface>()

    fun observeStatePlaylist(): LiveData<Playlist> = playlistStateLiveData
    fun observeStateTracksInPlaylist(): LiveData<List<Track>> = tracksInPlaylistStateLiveData
    fun observeStateAllTimeTracks(): LiveData<String> = allTimeTracksStateLiveData
    fun observeStateQuantityTracks(): LiveData<Int> = quantityTracksStateLiveData
    fun observeStateSharePlaylist(): LiveData<SharePlaylistStateInterface> =
        sharePlaylistStateLiveData

    fun getPlaylist() {
        viewModelScope.launch {
            playlistDbInteractor.getPlaylistById(playlistId).collect { playlistById ->
                playlist = playlistById
                playlistStateLiveData.postValue(playlist!!)
                getTrackInPlaylist(playlist!!)
            }
        }
    }

    private fun getTrackInPlaylist(playlist: Playlist) {
        if (playlist.tracksList.isNullOrEmpty()) {
            getTrackTime(arrayListOf())
            getQuantityTrack(0)
            return
        }
        viewModelScope.launch {
            playlistInteractor.getTrackInPlaylist(playlist.tracksList!!).collect() {
                trackInPlaylist = it
                tracksInPlaylistStateLiveData.postValue(it.reversed())
                getTrackTime(it)
                getQuantityTrack(playlist.quantityTracks)
            }
        }
    }

    private fun getTrackTime(trackInPlaylist: List<Track>?) {
        var trackTime = 0
        if (trackInPlaylist!!.isEmpty()) trackTime = 0
        else {
            trackInPlaylist.forEach { track ->
                trackTime += track.trackTimeMillis.toInt()
            }
        }

        val trackTimeMinutes = SimpleDateFormat("mm", Locale.getDefault()).format(trackTime)

        if (trackTimeMinutes.toInt() < 10) allTimeTracksStateLiveData.postValue(
            trackTimeMinutes.substring(
                1
            )
        )
        else allTimeTracksStateLiveData.postValue(trackTimeMinutes)
    }

    private fun getQuantityTrack(quantityTrack: Int) {
        quantityTracksStateLiveData.postValue(quantityTrack)
    }

    fun deleteTrack(trackId: Int) {
        viewModelScope.launch {
            playlistDbInteractor.deletaTrackInPlaylist(
                playlist, trackId
            )
            getPlaylist()
        }
    }

    fun clickOnShare(wordTrack: String) {
        if (playlist!!.quantityTracks == 0) {
            sharePlaylistStateLiveData.postValue(SharePlaylistStateInterface.playlistIsEmpity)
        } else {
            playlistInteractor.sharePlaylist(shareMessage(wordTrack))
        }
    }

    private fun shareMessage(wordTrack: String): String {
        var trackMessage: String = ""
        var i = 1
        trackInPlaylist.forEach(){track ->
            trackMessage += i.toString() + "." + " " + track.artistName + " - " + track.trackName +
                    " " + "(" + TimeUtils.formatTrackDuraction(track.trackTimeMillis.toInt()) + ")" + "\n"
            i++
        }
        val message: String =
            playlist!!.playListName + "\n" + playlist!!.playlistDescription +
                    "\n" + wordTrack + "\n" + trackMessage

        return message
    }

    fun deletePlaylist() {
        viewModelScope.launch {
            playlistDbInteractor.deletePlayList(playlist!!)
        }
    }

    fun editPlaylist() {

    }
}