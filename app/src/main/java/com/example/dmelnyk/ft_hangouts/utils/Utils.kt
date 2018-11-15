package com.example.dmelnyk.ft_hangouts.utils

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.SettingManager
import java.util.*


object Utils {

    private var sTheme: String = SettingManager.KEY_HIVE

    fun setTheme(theme: String) {
        sTheme = theme
    }
    fun changeToTheme(activity: Activity, theme: String) {
        sTheme = theme
        activity.finish()
        activity.startActivity(Intent(activity, activity.javaClass))
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out)
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        when (sTheme) {
            SettingManager.KEY_HIVE -> activity.setTheme(R.style.AppTheme)
            SettingManager.KEY_UNION -> activity.setTheme(R.style.UnionTheme)
            SettingManager.KEY_ALLIANCE -> activity.setTheme(R.style.AllianceTheme)
            SettingManager.KEY_EMPIRE -> activity.setTheme(R.style.EmpireTheme)
        }
    }

    fun setLanguage(resources: Resources, language: String) {
        val res = resources
        val dm = res.displayMetrics
        val conf = res.configuration
        conf.locale = Locale(language)
        res.updateConfiguration(conf, dm)
    }
}