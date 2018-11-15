package com.example.dmelnyk.ft_hangouts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.SettingManager
import com.example.dmelnyk.ft_hangouts.utils.Utils
import kotlinx.android.synthetic.main.fragment_setting.*
import java.util.*
import kotlin.collections.HashMap

class SettingFragment: Fragment() {

    companion object {

        fun newInstance(): SettingFragment = SettingFragment()
    }

    private var language: String = SettingManager.KEY_DEFAULT
    private var theme: String = SettingManager.KEY_HIVE
    private lateinit var settingManager: SettingManager
    private val chatBgMap: HashMap<Int, Boolean> = hashMapOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cntx = context
        if (cntx != null) { settingManager = SettingManager(cntx) }
        language = settingManager.getLanguage()
        theme = settingManager.getTheme()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check_box_setting_default_language.isChecked = language == SettingManager.KEY_DEFAULT
        check_box_setting_default_language.setOnCheckedChangeListener { _, isChecked ->
            setLanguageRadioGroupEnabled(!isChecked)
        }
        setLanguageRadioGroupEnabled(language != SettingManager.KEY_DEFAULT)
        image_view_setting_lang_save.setOnClickListener { onLangSaveClick() }
        setThemeRadioGroupEnabled()
        image_view_setting_theme_save.setOnClickListener { onThemeSaveClick() }

        chatBgMap[image_chat_bg_none.id] = false
        chatBgMap[image_chat_bg_whats_app.id] = false
        chatBgMap[image_chat_bg_telegram.id] = false

        image_chat_bg_none.setOnClickListener {
            val isChecked = chatBgMap[image_chat_bg_none.id]
            if (isChecked != null && !isChecked) {
                it.background = resources.getDrawable(R.drawable.chat_background_on_select, activity?.theme)
                chatBgMap[image_chat_bg_none.id] = true
            } else {
                it.setBackgroundResource(0)
                chatBgMap[image_chat_bg_none.id] = false
            }
        }
        image_chat_bg_whats_app.setOnClickListener { it.background = resources.getDrawable(R.drawable.chat_background_on_select, activity?.theme) }
        image_chat_bg_telegram.setOnClickListener { it.background = resources.getDrawable(R.drawable.chat_background_on_select, activity?.theme) }
    }

    private fun setLanguageRadioGroupEnabled(isEnabled: Boolean) {
        radio_group_setting_lang.isEnabled = isEnabled
        radio_group_setting_lang.alpha = if (isEnabled) 1f else 0.3f
        if (isEnabled) {
            when (language) {
                SettingManager.KEY_EN -> radio_button_setting_lang_en.isChecked = true
                SettingManager.KEY_UK -> radio_button_setting_lang_uk.isChecked = true
                SettingManager.KEY_FR -> radio_button_setting_lang_fr.isChecked = true
            }
        }
    }

    private fun setThemeRadioGroupEnabled() {
        when (theme) {
            SettingManager.KEY_HIVE -> radio_button_setting_theme_hive.isChecked = true
            SettingManager.KEY_UNION -> radio_button_setting_theme_union.isChecked = true
            SettingManager.KEY_ALLIANCE -> radio_button_setting_theme_alliance.isChecked = true
            SettingManager.KEY_EMPIRE -> radio_button_setting_theme_empire.isChecked = true
        }
    }

    private fun onLangSaveClick() {
        try {
            language = if (check_box_setting_default_language.isChecked)
                SettingManager.KEY_DEFAULT
            else when (radio_group_setting_lang.checkedRadioButtonId) {
                radio_button_setting_lang_en.id -> SettingManager.KEY_EN
                radio_button_setting_lang_uk.id -> SettingManager.KEY_UK
                radio_button_setting_lang_fr.id -> SettingManager.KEY_FR
                else -> Locale.getDefault().language
            }
            settingManager.saveData(SettingManager.KEY_LANG, language)
            Utils.setLanguage(resources, language)
            Toast.makeText(context, resources.getString(R.string.add_contact_save), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, resources.getString(R.string.db_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun onThemeSaveClick() {
        try {
            theme = when (radio_group_setting_theme.checkedRadioButtonId) {
                radio_button_setting_theme_hive.id -> SettingManager.KEY_HIVE
                radio_button_setting_theme_union.id -> SettingManager.KEY_UNION
                radio_button_setting_theme_alliance.id -> SettingManager.KEY_ALLIANCE
                radio_button_setting_theme_empire.id -> SettingManager.KEY_EMPIRE
                else -> SettingManager.KEY_HIVE
            }
            settingManager.saveData(SettingManager.KEY_THEME, theme)
            activity?.let { Utils.changeToTheme(it, theme) }
            Toast.makeText(context, resources.getString(R.string.add_contact_save), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, resources.getString(R.string.db_error), Toast.LENGTH_LONG).show()
        }
    }
}