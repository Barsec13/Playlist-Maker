package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.models.NetworkResponse
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.media.domain.model.Track

class SearchPresenter(
    private val view: SearchViewActivity,
    private val searchInteractor: SearchInteractor,
    private val searchRouter: SearchRouter,
) {
    fun clickButtonClearHistory() {
        searchInteractor.clearHistory()
        view.hideHistoryList()
        view.refreshHistory(searchInteractor.tracksHistoryFromJson())
    }

    fun visibleHistoryTrack() {
        val historyTracks = tracksHistoryFromJson()
        view.hideKeyboard()
        view.hideMessageError()
        view.refreshHistory(historyTracks)
        view.hideLoad()

        if (historyTracks.isNotEmpty()) {
            view.showHistoryList()
        } else
            view.hideHistoryList()
    }

    fun loadTracks(searchText: String) {
        if (searchText.isEmpty()) return
        view.hideKeyboard()
        view.hideMessageError()
        view.showLoad()
        searchInteractor.loadTracks(
            searchText = searchText,
            onSuccess = { tracks ->
                view.hideLoad()
                view.showTracks(tracks)
                view.showMessageError(NetworkResponse.SuccessRequest())
            },
            noData = {
                view.hideLoad()
                view.showMessageError(NetworkResponse.NoData())
            },
            serverError = {
                view.hideLoad()
                view.showMessageError(NetworkResponse.ServerError())
            },
            noInternet = {
                view.hideLoad()
                view.showMessageError(NetworkResponse.NoInternet())
            }
        )
    }

    fun clearSearchText() {
        view.clearTextSearch()
        view.hideKeyboard()
        view.hideMessageError()
        view.hideLoad()
        view.showTracks(emptyList())
        visibleHistoryTrack()
    }

    fun btnArrowBackClick() {
        searchRouter.backView()
    }

    fun onTrackClick(track: Track, position: Int) {
        searchInteractor.addTrack(track, position)
        searchRouter.sendToMedia(track)
        view.refreshHistory(searchInteractor.tracksHistoryFromJson())
    }

    fun tracksHistoryFromJson(): List<Track>{
        return searchInteractor.tracksHistoryFromJson()
    }
}