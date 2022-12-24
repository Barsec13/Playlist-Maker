package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Ссылки на кнопки на главном экране
        val buttonSearch = findViewById<Button>(R.id.buttonSearch)
        val buttonMedia = findViewById<Button>(R.id.buttonMedia)
        val buttonSettings = findViewById<Button>(R.id.buttonSettings)

        //Заглушка на нажатие кнопки "Поиск" методом анонимного класса
        val buttonSearchClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                Toast.makeText(this@MainActivity, "Бегу искать музыку", Toast.LENGTH_SHORT).show()
            }
        }

        buttonSearch.setOnClickListener(buttonSearchClickListener)

        //Заглушка на нажатие кнопки "Медиатека" методом лямбда-выражение
        buttonMedia.setOnClickListener() {
            Toast.makeText(this@MainActivity, "Открываю вашу любимую музыку", Toast.LENGTH_SHORT).show()
        }

        //Заглушка на нажатие кнопки "Настройки" методом лямбда-выражение
        buttonSettings.setOnClickListener() {
            Toast.makeText(this@MainActivity, "Сейчас все настроим", Toast.LENGTH_SHORT).show()
        }
    }
}