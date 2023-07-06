package com.practicum.playlistmaker.search.domain.models

import com.practicum.playlistmaker.player.domain.model.Track

sealed class ResultLoadTracks(){
    class OnSuccess(val data: List<Track>): ResultLoadTracks()
    class NoData : ResultLoadTracks()
    class ServerError : ResultLoadTracks()
    class NoInternet : ResultLoadTracks()
}