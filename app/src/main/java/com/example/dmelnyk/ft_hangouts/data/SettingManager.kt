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

        const val KEY_CHAT_BG = "chatBg"
        const val KEY_NONE = "none"
        const val KEY_WHATS_APP = "whatsApp"
        const val KEY_TELEGRAM = "telegram"

        const val KEY_BG_DATE = "bgDate"

        val KEY_DEFAULT = Locale.getDefault().language.toString().toLowerCase()
    }

    private val sharedPreferences = context.getSharedPreferences(KEY_SETTING, Context.MODE_PRIVATE)

    fun getLanguage(): String = sharedPreferences.getString(KEY_LANG, KEY_DEFAULT)

    fun getTheme(): String = sharedPreferences.getString(KEY_THEME, KEY_HIVE)

    fun getChatBackground(): String = sharedPreferences.getString(KEY_CHAT_BG, KEY_WHATS_APP)

    fun saveData(key: String, data: String) {
        val editor = sharedPreferences.edit()
        editor.putString(key, data)
        editor.apply()
    }

    fun getTimeOnResume(): Long = sharedPreferences.getLong(KEY_BG_DATE, Date().time)

    fun saveTimeOnPause(time: Long) {
        val editor = sharedPreferences.edit()
        editor.putLong(KEY_BG_DATE, time)
        editor.apply()
    }
}