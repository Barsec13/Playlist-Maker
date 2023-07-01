package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.data.network.ResultLoadTracks
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchRepository {
    override fun loadTracks(searchText: String): Flow<ResultLoadTracks> = flow {
        val result = networkClient.loadTracks(searchText = searchText)
        when (result) {
            is ResultLoadTracks.OnSuccess -> emit(ResultLoadTracks.OnSuccess(result.data!!))
            is ResultLoadTracks.NoData -> emit(ResultLoadTracks.NoData())
            is ResultLoadTracks.NoInternet -> emit(ResultLoadTracks.NoInternet())
            is ResultLoadTracks.ServerError -> emit(ResultLoadTracks.ServerError())
        }
    }
}