package com.example.dmelnyk.ft_hangouts.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import kotlinx.android.synthetic.main.fragment_setting.*
import android.util.DisplayMetrics
import java.util.*


class SettingFragment: Fragment() {

    companion object {

        const val KEY_SETTING = "setting"
        const val KEY_LANG = "lang"
        const val KEY_EN = "en"
        const val KEY_UK = "uk"
        const val KEY_FR = "fr"
        const val KEY_DEFAULT = "default"

        fun newInstance(): SettingFragment = SettingFragment()
    }

    private var language: String = KEY_DEFAULT

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loadSetting()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_setting, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        check_box_setting_default_language.isChecked = language == KEY_DEFAULT
        check_box_setting_default_language.setOnCheckedChangeListener { _, isChecked ->
            setRadioGroupEnabled(!isChecked)
        }
        setRadioGroupEnabled(language != KEY_DEFAULT)
        image_view_setting_lang_save.setOnClickListener { onLangSaveClick() }
    }

    private fun setRadioGroupEnabled(isEnabled: Boolean) {
        radio_group_setting_lang.isEnabled = isEnabled
        radio_group_setting_lang.alpha = if (isEnabled) 1f else 0.3f
        if (isEnabled) {
            when (language) {
                KEY_EN -> radio_button_setting_lang_en.isChecked = true
                KEY_UK -> radio_button_setting_lang_uk.isChecked = true
                KEY_FR -> radio_button_setting_lang_fr.isChecked = true
            }
        }
    }

    private fun onLangSaveClick() {
        try {
            val sharedPreferences = activity?.getSharedPreferences(KEY_SETTING, Context.MODE_PRIVATE)
            val editor = sharedPreferences?.edit()
            language = if (check_box_setting_default_language.isChecked)
                KEY_DEFAULT
            else when (radio_group_setting_lang.checkedRadioButtonId) {
                radio_button_setting_lang_en.id -> KEY_EN
                radio_button_setting_lang_uk.id -> KEY_UK
                radio_button_setting_lang_fr.id -> KEY_FR
                else -> Locale.getDefault().language
            }
            editor?.putString(KEY_LANG, language)
            editor?.apply()
            updateResources()
            Toast.makeText(context, resources.getString(R.string.add_contact_save), Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(context, resources.getString(R.string.db_error), Toast.LENGTH_LONG).show()
        }
    }

    private fun loadSetting() {
        val sharedPreferences = activity?.getSharedPreferences(KEY_SETTING, Context.MODE_PRIVATE)
        language = if (sharedPreferences != null) sharedPreferences.getString(KEY_LANG, KEY_DEFAULT) else KEY_DEFAULT
    }

    private fun updateResources() {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(language)
        res.updateConfiguration(conf, dm)
        activity?.recreate()
    }
}