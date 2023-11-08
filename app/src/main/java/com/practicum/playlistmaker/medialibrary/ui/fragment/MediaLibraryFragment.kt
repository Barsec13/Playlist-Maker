package com.practicum.playlistmaker.medialibrary.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.android.material.tabs.TabLayoutMediator
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentLibraryMediaBinding
import com.practicum.playlistmaker.medialibrary.ui.adapter.MediaViewPagerAdapter

class MediaLibraryFragment: Fragment() {
    private lateinit var binding: FragmentLibraryMediaBinding
    private lateinit var tabMediator: TabLayoutMediator
    //private val mediaLibraryNavigationRouter = MediaLibraryNavigationRouter(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLibraryMediaBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewPager.adapter = MediaViewPagerAdapter(childFragmentManager, lifecycle)

        tabMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager){tab, position ->
            when(position){
                0 -> tab.text = resources.getString(R.string.favorite_track)
                else -> tab.text = resources.getString(R.string.playlist)
            }
        }

        tabMediator.attach()

        setListeners()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabMediator.detach()
    }

    private fun setListeners(){
//        binding.toolbarMediaLibrary.setOnClickListener() {
//            mediaLibraryNavigationRouter.backView()
//        }
    }
}