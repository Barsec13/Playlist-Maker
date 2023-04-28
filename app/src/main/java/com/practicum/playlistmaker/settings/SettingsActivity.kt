package com.practicum.playlistmaker.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.App
import com.practicum.playlistmaker.DARK_THEME_KEY
import com.practicum.playlistmaker.PLAY_LIST_MAKER_SHARED_PREFERENCES
import com.practicum.playlistmaker.R

class SettingsActivity : AppCompatActivity() {
    //Переменные для работы с UI
    lateinit var buttonShare: FrameLayout
    lateinit var buttonSupport: FrameLayout
    lateinit var buttonUserAgreement: FrameLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val buttonArrowBackSettings = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbarSetting)
        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
        buttonArrowBackSettings.setOnClickListener(){
            finish()
        }

        //Ссылки на кнопки
        buttonShare = findViewById(R.id.button_share)
        buttonSupport = findViewById(R.id.button_support)
        buttonUserAgreement = findViewById(R.id.button_user_agreement)

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

        //Реализация переключателя темы
        val themeSwitcher = findViewById<SwitchMaterial>(R.id.themeSwitcher)

        themeSwitcher.isChecked = getSharedPreferences(PLAY_LIST_MAKER_SHARED_PREFERENCES, MODE_PRIVATE)
            .getBoolean(DARK_THEME_KEY, false)

        themeSwitcher.setOnCheckedChangeListener {switcher, checked ->
           (applicationContext as App).switchTheme(checked)
            (applicationContext as App).saveTheme(checked)
        }
    }
}