package com.practicum.playlistmaker.main.ui.activity

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.main.ui.model.NavigationViewState
import com.practicum.playlistmaker.main.ui.router.NavigationRouter
import com.practicum.playlistmaker.main.ui.view_model.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {
    //Переменные для работы с UI
    lateinit var buttonSearch: Button
    lateinit var buttonMedia: Button
    lateinit var buttonSettings: Button

    private val mainViewModel: MainViewModel by viewModel()
    private val mediaRouter = NavigationRouter(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        setListeners()

        mainViewModel.observeNavigationViewState().observe(this) {
            navigation(it)
        }
    }

    private fun navigation(state: NavigationViewState) {
        when (state) {
            is NavigationViewState.Search -> mediaRouter.getSearch()
            is NavigationViewState.Settings -> mediaRouter.getSettings()
            is NavigationViewState.MediaLibrary -> mediaRouter.getMediaLibrary()
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
            mainViewModel.onMediaLibraryView()
        }

        buttonSettings.setOnClickListener() {
            mainViewModel.onSettingsView()
        }
    }
}