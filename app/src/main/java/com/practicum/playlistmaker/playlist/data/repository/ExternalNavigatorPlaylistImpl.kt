package com.practicum.playlistmaker.playlist.data.repository

import android.content.Context
import android.content.Intent
import com.practicum.playlistmaker.playlist.domain.api.ExternalNavigatorPlaylist


class ExternalNavigatorPlaylistImpl(private val context: Context): ExternalNavigatorPlaylist {
    override fun shareLink(message: String) {
        try {
            context.startActivity(
                Intent(Intent.ACTION_SEND).apply {
                    type = "text/plain"
                    putExtra(Intent.EXTRA_TEXT, message)
                    setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                })
        }

        catch (_: Exception){ }
    }
}