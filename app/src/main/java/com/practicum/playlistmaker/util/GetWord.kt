package com.practicum.playlistmaker.util

import android.view.View
import com.practicum.playlistmaker.R

object GetWord {
    fun getWordTrack(count: Int, view: View): String {
        count % 100
        if (count in 5..20) return view.context.getString(R.string._5_20_tracks)
        count % 10
        when (count) {
            1 -> return view.context.getString(R.string._1_track)

            in 2..4 -> return view.context.getString(R.string._2_4_track)

            else -> return view.context.getString(R.string._5_20_tracks)
        }
    }

    fun getWordTime(count: Int, view: View): String{
        count % 100
        if (count in 5..20) return view.context.getString(R.string._5_20_minutes)
        count % 10
        when (count) {
            1 -> return view.context.getString(R.string._1_minutes)

            in 2..4 -> return view.context.getString(R.string._2_4_minutes)

            else -> return view.context.getString(R.string._5_20_minutes)
        }
    }

}