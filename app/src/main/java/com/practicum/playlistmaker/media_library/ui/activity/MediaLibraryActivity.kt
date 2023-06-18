package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.ActivityLibraryMediaBinding
import com.practicum.playlistmaker.media_library.ui.adapter.MediaViewPagerAdapter
import com.practicum.playlistmaker.media_library.ui.router.MediaLibraryNavigationRouter


class MediaLibraryActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    private val mediaLibraryNavigationRouter = MediaLibraryNavigationRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.favorite_track)
                else -> tab.text = resources.getString(R.string.playlist)
            }
        }

        tabMediator.attach()

        setListeners()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabMediator.detach()
    }

    private fun setListeners(){
        binding.toolbarMediaLibrary.setOnClickListener() {
            mediaLibraryNavigationRouter.backView()
        }
    }
}