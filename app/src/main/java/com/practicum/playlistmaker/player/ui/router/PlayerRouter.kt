package com.practicum.playlistmaker.player.ui.router

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.Constant
import com.practicum.playlistmaker.player.domain.model.Track

class PlayerRouter(private val activity: AppCompatActivity) {
    fun backView() {
        activity.finish()
    }

    fun getToMedia(): Track {
        return Gson().fromJson(
            activity.intent.getStringExtra(Constant.SEND_TRACK),
            Track::class.java
        )
    }
}