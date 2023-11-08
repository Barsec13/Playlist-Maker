package com.practicum.playlistmaker.medialibrary.ui.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.db.domain.api.FavoriteTrackInteractor
import com.practicum.playlistmaker.medialibrary.ui.models.FavoriteStateInterface
import kotlinx.coroutines.launch

class FavoriteTrackViewModel(
    private val favoriteTrackInteractor: FavoriteTrackInteractor,
) : ViewModel() {

    private val stateLiveData = MutableLiveData<FavoriteStateInterface>()
    fun observeState(): LiveData<FavoriteStateInterface> = stateLiveData

    private fun renderState(state: FavoriteStateInterface) {
        stateLiveData.postValue(state)
    }

    fun showFavoriteTrack(){
        viewModelScope.launch {
            favoriteTrackInteractor.getFavoriteTracks().collect(){
                favoriteTracks ->
                if (favoriteTracks.isEmpty()) renderState(FavoriteStateInterface.FavoriteTracksIsEmpty)
                else renderState(FavoriteStateInterface.FavoriteTracks(favoriteTracks = favoriteTracks.reversed()))
            }

        }
    }
}