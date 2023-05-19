package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.models.NetworkError
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkClientImpl(
    private val api: iTunesSearchAPI,
) : NetworkClient {
    override fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: (NetworkError) -> Unit,
        serverError: (NetworkError) -> Unit,
        noInternet: (NetworkError) -> Unit,
    ) {
        api.searchTrack(searchText)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>,
                ) {
                    if (response.code() == 200) {
                        if (!response.body()?.results.isNullOrEmpty()) {
                            onSuccess.invoke(response.body()?.results!!)
                        } else noData.invoke(NetworkError.NoData())
                    } else serverError.invoke(NetworkError.ServerError())
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    noInternet.invoke(NetworkError.NoInternet())
                }
            })
    }
}
