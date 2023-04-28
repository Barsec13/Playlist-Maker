package com.practicum.playlistmaker.main

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.practicum.playlistmaker.media.presentation.MediaActivity
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.search.presentation.SearchActivity
import com.practicum.playlistmaker.settings.SettingsActivity

class MainActivity : AppCompatActivity() {
    //Переменные для работы с UI
    lateinit var buttonSearch:Button
    lateinit var buttonMedia:Button
    lateinit var buttonSettings:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ссылки на кнопки на главном экране
        buttonSearch = findViewById(R.id.buttonSearch)
        buttonMedia = findViewById(R.id.buttonMedia)
        buttonSettings = findViewById(R.id.buttonSettings)

        buttonSearch.setOnClickListener() {
            val searchIntent = Intent(this@MainActivity, SearchActivity::class.java)
            startActivity(searchIntent)
        }

        buttonMedia.setOnClickListener() {
            val mediaIntent = Intent(this@MainActivity, MediaActivity::class.java)
            startActivity(mediaIntent)
        }

        buttonSettings.setOnClickListener() {
            val settingsIntent = Intent(this@MainActivity, SettingsActivity::class.java)
            startActivity(settingsIntent)
        }
    }
}