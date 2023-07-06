package com.practicum.playlistmaker.search.data.repository

import com.practicum.playlistmaker.search.data.network.NetworkClient
import com.practicum.playlistmaker.search.domain.api.SearchRepository
import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class SearchRepositoryImpl(
    private val networkClient: NetworkClient,
) : SearchRepository {
    override fun loadTracks(searchText: String): Flow<ResultLoadTracks> = flow {
        emit(networkClient.loadTracks(searchText = searchText))
    }
}