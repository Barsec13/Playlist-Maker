package com.practicum.playlistmaker.search.domain.models

//Случаи ошибок или результатов при запросе
sealed class NetworkError() {
    class ServerError() : NetworkError()

    class NoData() : NetworkError()

    class NoInternet() : NetworkError()
}