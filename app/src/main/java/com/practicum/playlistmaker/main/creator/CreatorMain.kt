package com.practicum.playlistmaker.main.creator

import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.main.ui.router.NavigationRouter

object CreatorMain {
    fun getNavigationRouter(activity: AppCompatActivity): NavigationRouter {
        return NavigationRouter(activity)
    }
}