package com.practicum.playlistmaker.media_library.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.media_library.ui.fragment.FavoriteTrackFragment
import com.practicum.playlistmaker.media_library.ui.fragment.PlaylistsFragment

class MediaViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle)
    : FragmentStateAdapter(fragmentManager, lifecycle) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> FavoriteTrackFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}