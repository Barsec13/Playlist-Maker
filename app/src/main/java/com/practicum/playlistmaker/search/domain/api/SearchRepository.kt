package com.practicum.playlistmaker.search.domain.api

import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import kotlinx.coroutines.flow.Flow


interface SearchRepository {
    fun loadTracks(searchText: String): Flow<ResultLoadTracks>
}