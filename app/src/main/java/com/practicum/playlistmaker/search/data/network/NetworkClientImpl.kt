package com.practicum.playlistmaker.search.data.network

import com.practicum.playlistmaker.domain.models.Track
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class NetworkClientImpl(
    private val api: iTunesSearchAPI
): NetworkClient {
    override fun loadTracks(
        searchText: String,
        onSuccess: (List<Track>) -> Unit,
        noData: () -> Unit,
        serverError: () -> Unit,
        noInternet: () -> Unit
    ){
        api.searchTrack(searchText)
            .enqueue(object : Callback<TrackResponse> {
                override fun onResponse(
                    call: Call<TrackResponse>,
                    response: Response<TrackResponse>
                ) {
                    if (response.code() == 200) {
                        if (!response.body()?.results.isNullOrEmpty()){
                            onSuccess.invoke(response.body()?.results!!)
                        }
                        else noData.invoke()
                    } else serverError.invoke()
                }

                override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                    noInternet.invoke()
                }
            })
    }
}