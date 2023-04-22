package com.practicum.playlistmaker

import java.text.SimpleDateFormat
import java.util.*

object TimeUtils {
    fun formatTrackDuraction(time: Int): String {
        return SimpleDateFormat("mm:ss", Locale.getDefault()).format(time)
    }
}