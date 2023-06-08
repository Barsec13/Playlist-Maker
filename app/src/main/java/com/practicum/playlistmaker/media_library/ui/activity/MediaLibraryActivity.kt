package com.practicum.playlistmaker.media_library.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.databinding.ActivityLibraryMediaBinding
import com.practicum.playlistmaker.media_library.ui.adapter.MediaViewPagerAdapter
import com.practicum.playlistmaker.settings.creator.CreatorSettings

class MediaLibraryActivity() : AppCompatActivity() {

    private lateinit var binding: ActivityLibraryMediaBinding
    private lateinit var tabMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLibraryMediaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewPager.adapter = MediaViewPagerAdapter(supportFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = "Избранные треки"
                else -> tab.text = "Плейлисты"
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
            CreatorSettings.getSettingsNavigationRouter(this).backView()
        }
    }
}