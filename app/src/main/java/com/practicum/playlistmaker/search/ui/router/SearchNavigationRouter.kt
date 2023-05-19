package com.practicum.playlistmaker.search.ui.router

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.Constant
import com.practicum.playlistmaker.player.domain.model.Track
import com.practicum.playlistmaker.player.ui.activity.PlayerActivity

class SearchNavigationRouter(private val activity: AppCompatActivity) {
    fun sendToMedia(track: Track) {
        val searchIntent = Intent(activity, PlayerActivity::class.java).apply {
            putExtra(Constant.SEND_TRACK, Gson().toJson(track))
        }
        activity.startActivity(searchIntent)
    }

    fun backView() {
        activity.finish()
    }
}