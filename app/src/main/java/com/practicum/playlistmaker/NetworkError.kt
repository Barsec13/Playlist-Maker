package com.practicum.playlistmaker

//Случаи ошибок или результатов при запросе
sealed class NetworkError(){
    class SuccessRequest() : NetworkError()

    class ServerError() : NetworkError()

    class NoData() : NetworkError()

    class NoInternet() : NetworkError()
}