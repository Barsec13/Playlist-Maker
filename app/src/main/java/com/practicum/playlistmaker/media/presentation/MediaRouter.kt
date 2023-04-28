package com.practicum.playlistmaker.media.presentation

import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.practicum.playlistmaker.Constant
import com.practicum.playlistmaker.domain.models.Track

class MediaRouter(private val activity: AppCompatActivity){
    fun backView(){
        activity.finish()

    }

    fun getToMedia(): Track{
        return Gson().fromJson(activity.intent.getStringExtra(Constant.SEND_TRACK), Track::class.java)
    }
}