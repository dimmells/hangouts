package com.example.dmelnyk.ft_hangouts.data

import android.content.Context
import java.util.*

class SettingManager(private val context: Context) {

    companion object {
        const val KEY_SETTING = "setting"

        const val KEY_LANG = "lang"
        const val KEY_EN = "en"
        const val KEY_UK = "uk"
        const val KEY_FR = "fr"

        const val KEY_THEME = "theme"
        const val KEY_HIVE = "hive"
        const val KEY_UNION = "union"
        const val KEY_ALLIANCE = "alliance"
        const val KEY_EMPIRE = "empire"

        val KEY_DEFAULT = Locale.getDefault().language.toString().toLowerCase()
    }

    private val sharedPreferences = context.getSharedPreferences(SettingManager.KEY_SETTING, Context.MODE_PRIVATE)

    fun getLanguage(): String = sharedPreferences.getString(SettingManager.KEY_LANG, SettingManager.KEY_DEFAULT)

    fun getTheme(): String = sharedPreferences.getString(SettingManager.KEY_THEME, SettingManager.KEY_HIVE)

    fun saveData(key: String, data: String) {
        val sharedPreferences = context.getSharedPreferences(SettingManager.KEY_SETTING, Context.MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putString(key, data)
        editor?.apply()
    }
}