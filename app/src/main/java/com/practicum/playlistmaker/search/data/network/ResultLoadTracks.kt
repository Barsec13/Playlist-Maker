package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.player.domain.model.Track

sealed class ResultLoadTracks(val data: List<Track>? = null){
    class OnSuccess(data: List<Track>): ResultLoadTracks(data)
    class NoData : ResultLoadTracks()
    class ServerError : ResultLoadTracks()
    class NoInternet : ResultLoadTracks()
}