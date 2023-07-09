package com.practicum.playlistmaker.media_library.ui.models

import com.practicum.playlistmaker.player.domain.model.Track

sealed interface FavoriteStateInterface {
    object FavoriteTracksIsEmpty : FavoriteStateInterface

    data class FavoriteTracks(
        val favoriteTracks: List<Track>,
    ) : FavoriteStateInterface
}