package com.practicum.playlistmaker.search.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.search.domain.api.SearchInteractor
import com.practicum.playlistmaker.search.domain.models.NetworkError
import com.practicum.playlistmaker.search.domain.models.ResultLoadTracks
import com.practicum.playlistmaker.search.ui.models.SearchStateInterface
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val searchInteractor: SearchInteractor): ViewModel() {

    companion object {
        private const val SEARCH_DEBOUNCE_DELAY_MILLIS = 2000L
    }

    private var latestSearchText: String? = null
    private var isClickAllowed = true

    private var searchJob: Job? = null

    private val stateLiveData = MutableLiveData<SearchStateInterface>()

    fun observeState(): LiveData<SearchStateInterface> = stateLiveData

    private fun renderState(state: SearchStateInterface) {
        stateLiveData.postValue(state)
    }

    override fun onCleared() {
        searchJob?.cancel()
    }

    //Запускаем поиск, если пользователь 2 секунды не вводит текст
    fun onTextChanged(
        changedText: String,
        focus: Boolean) {

        if (latestSearchText == changedText ||
            !focus || changedText.isEmpty()) return

        renderState(SearchStateInterface.changeTextSearch)

        this.latestSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
            loadTracks(changedText)
        }
    }

    //Ограничение двойного нажатия на трек для открытия плеера
    fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false

            viewModelScope.launch {
                delay(SEARCH_DEBOUNCE_DELAY_MILLIS)
                isClickAllowed = true
            }
        }
        return current
    }

    fun loadTracks(searchText: String) {
        if (searchText.isEmpty()) return
        renderState(SearchStateInterface.Loading)

        viewModelScope.launch {
            searchInteractor
                .loadTracks(searchText)
                .collect { result: ResultLoadTracks ->
                    when (result) {
                        is ResultLoadTracks.OnSuccess -> renderState(
                            SearchStateInterface.SearchTracks(
                                result.data
                            )
                        )

                        is ResultLoadTracks.NoData -> renderState(
                            SearchStateInterface.Error(
                                NetworkError.NoData()
                            )
                        )

                        is ResultLoadTracks.NoInternet -> renderState(
                            SearchStateInterface.Error(
                                NetworkError.NoInternet()
                            )
                        )

                        is ResultLoadTracks.ServerError -> renderState(
                            SearchStateInterface.Error(
                                NetworkError.ServerError()
                            )
                        )
                    }
                }
        }
    }

    fun visibleHistoryTrack() {
        tracksHistoryFromJson()
    }

    fun clickButtonClearHistory() {
        searchInteractor.clearHistory()
        visibleHistoryTrack()
    }

    fun clearSearchText() {
        visibleHistoryTrack()
    }

    fun clearLatestTextSearch(){
        latestSearchText = null
    }

    private fun tracksHistoryFromJson() {
        viewModelScope.launch {
            renderState(
                SearchStateInterface.HistoryTracks(searchInteractor.tracksHistoryFromJson())
            )
        }
    }

    fun onTrackClick(track: Track, position: Int) {
        if (clickDebounce()) {
            searchInteractor.addTrack(track, position)
        }
    }
}