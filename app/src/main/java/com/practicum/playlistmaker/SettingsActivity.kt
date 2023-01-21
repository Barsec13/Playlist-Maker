package com.practicum.playlistmaker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
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
        val share = findViewById<ImageView>(R.id.share)
        val support = findViewById<ImageView>(R.id.support)
        val userAgreement = findViewById<ImageView>(R.id.user_agreement)

        //Реализация кнопки "Поделиться приложением"
        share.setOnClickListener() {
            val linkToTheCourse = getString(R.string.link_to_the_course)
            val shareIntent = Intent(Intent.ACTION_SEND)
            shareIntent.setType("text/plain")
            shareIntent.putExtra(Intent.EXTRA_TEXT, linkToTheCourse)
            startActivity(shareIntent)
        }

        //Реализация кнопки "Поддержка"
        support.setOnClickListener(){
            val message = getString(R.string.message)
            val subject = getString(R.string.subject)
            val shareIntent = Intent(Intent.ACTION_SENDTO)
            val emailDeveloper = getString(R.string.email_developer)
            shareIntent.data = Uri.parse("mailto:")
            shareIntent.putExtra(Intent.EXTRA_EMAIL, arrayOf("putilov0407@gmail.com"))
            shareIntent.putExtra(Intent.EXTRA_SUBJECT, subject)
            shareIntent.putExtra(Intent.EXTRA_TEXT, message)
            startActivity(shareIntent)
        }

        //Реализация кнопки пользовательское соглашение
        userAgreement.setOnClickListener(){
            val linkToTheOffer = getString(R.string.link_to_the_offer)
            val uriUserAgreement = Uri.parse(linkToTheOffer)
            val intent = Intent(Intent.ACTION_VIEW, uriUserAgreement)
            startActivity(intent)
        }
    }
}