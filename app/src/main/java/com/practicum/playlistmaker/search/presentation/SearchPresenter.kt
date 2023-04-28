package com.practicum.playlistmaker.search.presentation

import com.practicum.playlistmaker.search.domain.models.NetworkError
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.domain.models.Track

class SearchPresenter(
    private val view: SearchViewActivity,
    private val searchInteractor: SearchInteractor,
    private val searchRouter: SearchRouter
) {
    fun clickButtonClearHistory(){
        searchInteractor.clearHistory()
        view.refreshHistory(searchInteractor.tracksHistoryFromJson())
    }

    fun visibleHistoryTrack(historyTracks:List<Track>){
        view.hideKeyboard()
        view.hideMessageError()
        view.refreshHistory(historyTracks)
        view.hideLoad()

        if (historyTracks.isNotEmpty()) {
            view.showHistoryList()
        }
        else
            view.hideHistoryList()
    }

    fun loadTracks(searchText: String){
        if (searchText.isEmpty()) return
        view.hideKeyboard()
        view.hideMessageError()
        view.showLoad()
        searchInteractor.loadTracks(
            searchText = searchText,
            onSuccess = {tracks ->
                view.hideLoad()
                view.showTracks(tracks)
                view.showMessageError(NetworkError.SuccessRequest())
            },
            noData = {
                view.hideLoad()
                view.showMessageError(NetworkError.NoData())
            },
            serverError = {
                view.hideLoad()
                view.showMessageError(NetworkError.ServerError())
            },
            noInternet = {
                view.hideLoad()
                view.showMessageError(NetworkError.NoInternet())
            }
        )
    }

    fun clearSearchText() {
        view.clearTextSearch()
        view.hideKeyboard()
        view.hideMessageError()
        view.hideLoad()
        view.showTracks(emptyList())
        visibleHistoryTrack(searchInteractor.tracksHistoryFromJson())
    }

    fun btnArrowBackClick(){
        searchRouter.backView()
    }

    fun onTrackClick(track: Track, position: Int) {
        searchInteractor.addTrack(track, position)
        searchRouter.sendToMedia(track)
        view.refreshHistory(searchInteractor.tracksHistoryFromJson())
    }
}