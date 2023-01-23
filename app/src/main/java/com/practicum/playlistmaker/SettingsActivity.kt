package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
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

        //Ссылки на кнопки
        val buttonShare = findViewById<FrameLayout>(R.id.button_share)
        val buttonSupport = findViewById<FrameLayout>(R.id.button_support)
        val buttonUserAgreement = findViewById<FrameLayout>(R.id.button_user_agreement)

        //Реализация кнопки "Поделиться приложением"
        buttonShare.setOnClickListener() {
            Intent(Intent.ACTION_SEND).apply {
                val linkToTheCourse = getString(R.string.link_to_the_course)
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, linkToTheCourse)
                startActivity(this)
            }
        }

        //Реализация кнопки "Поддержка"
        buttonSupport.setOnClickListener(){
            Intent(Intent.ACTION_SEND).apply{
                val message = getString(R.string.message)
                val subject = getString(R.string.subject)
                val emailDeveloper = getString(R.string.email_developer)
                data = Uri.parse("mailto:")
                putExtra(Intent.EXTRA_EMAIL, arrayOf(emailDeveloper))
                putExtra(Intent.EXTRA_SUBJECT, subject)
                putExtra(Intent.EXTRA_TEXT, message)
                startActivity(this)
            }
        }

        //Реализация кнопки пользовательское соглашение
        buttonUserAgreement.setOnClickListener(){
            val linkToTheOffer = getString(R.string.link_to_the_offer)
            val uriUserAgreement = Uri.parse(linkToTheOffer)
            val intent = Intent(Intent.ACTION_VIEW, uriUserAgreement)
            startActivity(intent)
        }
    }
}