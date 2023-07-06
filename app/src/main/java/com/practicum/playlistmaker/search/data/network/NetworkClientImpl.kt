package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class NetworkClientImpl(
    private val api: iTunesSearchAPI,
    private val checkConnection: CheckConnection,
) : NetworkClient {
    override suspend fun loadTracks(
        searchText: String,
    ): ResultLoadTracks {
        if (!checkConnection.isConnected()) {
            return ResultLoadTracks.NoInternet()
        }

        return withContext(Dispatchers.IO)
        {
            try {
                val response = api.searchTrack(searchText)
                if (!response.body()?.results.isNullOrEmpty()) {
                    ResultLoadTracks.OnSuccess(response.body()!!.results)

                } else {
                    ResultLoadTracks.NoData()
                }
            } catch (e: Throwable) {
                ResultLoadTracks.ServerError()
            }
        }
    }
}