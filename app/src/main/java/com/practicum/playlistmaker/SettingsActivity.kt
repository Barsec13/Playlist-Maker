package com.practicum.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        //Кнопка "<-" из окна "Настройки"
        val buttonArrowBackSettings = findViewById<Button>(R.id.buttonArrowBackSettings)
        //Обработка нажатия на кнопку "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener(){
            finish()
        }
    }
}