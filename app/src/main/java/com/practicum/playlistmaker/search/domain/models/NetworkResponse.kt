package com.practicum.playlistmaker.search.domain.models

//Случаи ошибок или результатов при запросе
sealed class NetworkResponse() {
    class SuccessRequest() : NetworkResponse()

    class ServerResponse() : NetworkResponse()

    class NoData() : NetworkResponse()

    class NoInternet() : NetworkResponse()
}