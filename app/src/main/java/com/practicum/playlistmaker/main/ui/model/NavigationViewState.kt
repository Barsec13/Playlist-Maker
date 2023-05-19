package com.practicum.playlistmaker.main.ui.model

sealed interface NavigationViewState {
    object Search : NavigationViewState
    object MediaLibrary : NavigationViewState
    object Settings : NavigationViewState
}