package com.practicum.playlistmaker.util

import java.text.SimpleDateFormat
import java.util.Locale

object TimeUtils {
    fun formatTrackDuraction(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}