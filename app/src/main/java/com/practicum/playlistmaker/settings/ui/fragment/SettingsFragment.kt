package com.practicum.playlistmaker.settings.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.Fragment
import com.google.android.material.switchmaterial.SwitchMaterial
import com.practicum.playlistmaker.databinding.FragmentSettingsBinding
import com.practicum.playlistmaker.settings.ui.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment: Fragment() {
    private lateinit var buttonShare: FrameLayout
    private lateinit var buttonSupport: FrameLayout
    private lateinit var buttonUserAgreement: FrameLayout
    private lateinit var themeSwitcher: SwitchMaterial
    private lateinit var buttonArrowBackSettings: androidx.appcompat.widget.Toolbar

    private val viewModelSettings: SettingsViewModel by viewModel()
    //private val settingsNavigationRouter = SettingsNavigationRouter(this)
    private lateinit var binding: FragmentSettingsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()

        setListeners()

        //Отображение актуального состояния переключателя
        viewModelSettings.observeThemeSwitcherChecked().observe(viewLifecycleOwner) { isChecked ->
            themeSwitcher.isChecked = isChecked
        }
    }

    private fun initViews() {
        buttonShare = binding.buttonShare
        buttonSupport = binding.buttonSupport
        buttonUserAgreement = binding.buttonUserAgreement
        themeSwitcher = binding.themeSwitcher
        buttonArrowBackSettings = binding.toolbarSetting
    }

    private fun setListeners() {
        //Реализация переключения темы
        themeSwitcher.setOnCheckedChangeListener { _, isChecked ->
            viewModelSettings.clickThemeSwitcher(isChecked)
        }

        //Реализация нажатия кнопки "Поделиться приложением"
        buttonShare.setOnClickListener() {
            viewModelSettings.clickShareApp()
        }

        //Реализация кнопки "Поддержка"
        buttonSupport.setOnClickListener() {
            viewModelSettings.clickApplySupport()

        }

        //Реализация кнопки пользовательское соглашение
        buttonUserAgreement.setOnClickListener() {
            viewModelSettings.clickViewUserAgreement()
        }

        //Обработка нажатия на ToolBar "<-" и переход
        // на главный экран через закрытие экрана "Настройки"
//        buttonArrowBackSettings.setOnClickListener() {
//            settingsNavigationRouter.backView()
//        }
    }
}