//package com.practicum.playlistmaker.settings.ui.activity
//
//import android.os.Bundle
//import android.widget.FrameLayout
//import androidx.appcompat.app.AppCompatActivity
//import com.google.android.material.switchmaterial.SwitchMaterial
//import com.practicum.playlistmaker.R
//import com.practicum.playlistmaker.settings.ui.router.SettingsNavigationRouter
//import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
//import org.koin.androidx.viewmodel.ext.android.viewModel
//
//class SettingsActivity : AppCompatActivity() {
//    private lateinit var buttonShare: FrameLayout
//    private lateinit var buttonSupport: FrameLayout
//    private lateinit var buttonUserAgreement: FrameLayout
//    private lateinit var themeSwitcher: SwitchMaterial
//    private lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar
//
//    private val viewModelSettings: SettingsViewModel by viewModel()
//    private val settingsNavigationRouter = SettingsNavigationRouter(this)
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.fragment_settings)
//
//        initViews()
//
//        setListeners()
//
//        //Отображение актуального состояния переключателя
//        viewModelSettings.observeThemeSwitcherChecked().observe(this) { isChecked ->
//            themeSwitcher.isChecked = isChecked
//        }
//    }
//
//    private fun initViews() {
//        buttonShare = findViewById(R.id.button_share)
//        buttonSupport = findViewById(R.id.button_support)
//        buttonUserAgreement = findViewById(R.id.button_user_agreement)
//        themeSwitcher = findViewById(R.id.themeSwitcher)
//        buttonArrowBackSettings = findViewById(R.id.toolbarSetting)
//    }
//
//    private fun setListeners() {
//        //Реализация переключения темы
//        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
//            viewModelSettings.clickThemeSwitcher(isChecked)
//        }
//
//        //Реализация нажатия кнопки "Поделиться приложением"
//        buttonShare.setOnClickListener() {
//            viewModelSettings.clickShareApp()
//        }
//
//        //Реализация кнопки "Поддержка"
//        buttonSupport.setOnClickListener() {
//            viewModelSettings.clickApplySupport()
//
//        }
//
//        //Реализация кнопки пользовательское соглашение
//        buttonUserAgreement.setOnClickListener() {
//            viewModelSettings.clickViewUserAgreement()
//        }
//
//        //Обработка нажатия на ToolBar "<-" и переход
//        // на главный экран через закрытие экрана "Настройки"
//        buttonArrowBackSettings.setOnClickListener() {
//            settingsNavigationRouter.backView()
//        }
//    }
//}