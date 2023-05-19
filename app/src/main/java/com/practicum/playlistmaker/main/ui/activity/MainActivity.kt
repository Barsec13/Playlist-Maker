package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.creator.CreatorMain
import com.practicum.playlistmaker.main.ui.model.NavigationViewState
import com.practicum.playlistmaker.main.ui.router.NavigationRouter
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel

class MainActivity : AppCompatActivity() {
    //Переменные для работы с UI
    lateinit var buttonSearch: Button
    lateinit var buttonMedia: Button
    lateinit var buttonSettings: Button
    private lateinit var mainViewModel: MainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        setListeners()

        mainViewModel = ViewModelProvider(
            this,
            MainViewModel.getViewModelFactory()
        )[MainViewModel::class.java]

        mainViewModel.observeNavigationViewState().observe(this) {
            navigation(it)
        }
    }

    private fun navigation(state: NavigationViewState) {
        when (state) {
            is NavigationViewState.Search -> getNavigationRouter().getSearch()
            is NavigationViewState.Settings -> getNavigationRouter().getSettings()
            is NavigationViewState.MediaLibrary -> getNavigationRouter().getMediaLibrary()
        }
    }

    private fun initViews() {
        //Ссылки на кнопки на главном экране
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonMedia = findViewById(R.id.buttonMedia)
        buttonSettings = findViewById(R.id.buttonSettings)
    }

    private fun setListeners() {
        buttonSearch.setOnClickListener() {
            mainViewModel.onSearchView()
        }

        buttonMedia.setOnClickListener() {
            mainViewModel.onSettingsView()
        }

        buttonSettings.setOnClickListener() {
            mainViewModel.onMediaLibraryView()
        }
    }

    private fun getNavigationRouter(): NavigationRouter {
        return CreatorMain.getNavigationRouter(this)
    }
}