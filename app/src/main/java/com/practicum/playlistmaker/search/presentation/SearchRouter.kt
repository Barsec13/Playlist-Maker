package com.practicum.playlistmaker.search.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.Constant.SEND_TRACK
import com.practicum.playlistmaker.media.domain.model.Track
import com.practicum.playlistmaker.media.presentation.MediaActivity

class SearchRouter(
    private val activity: AppCompatActivity,
) {

    fun sendToMedia(track: Track) {
        val searchIntent = Intent(activity, MediaActivity::class.java).apply {
            putExtra(SEND_TRACK, Gson().toJson(track))
        }
        activity.startActivity(searchIntent)
    }

    fun backView() {
        activity.finish()
    }
}