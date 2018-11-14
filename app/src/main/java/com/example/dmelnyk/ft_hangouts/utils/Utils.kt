package com.example.dmelnyk.ft_hangouts.utils

import android.app.Activity
import android.content.Intent
import android.util.Log
import com.example.dmelnyk.ft_hangouts.R


object Utils {
    private var sTheme: Int = 0

    const val THEME_MATERIAL_LIGHT = 0
    const val THEME_YOUR_CUSTOM_THEME = 1

    fun changeToTheme(activity: Activity, theme: Int) {
        sTheme = theme
        activity.finish()
        activity.startActivity(Intent(activity, activity.javaClass))
        activity.overridePendingTransition(android.R.anim.fade_in,
                android.R.anim.fade_out)
    }

    fun onActivityCreateSetTheme(activity: Activity) {
        Log.i("theme", sTheme.toString())
        when (sTheme) {
            THEME_MATERIAL_LIGHT -> activity.setTheme(R.style.AppTheme)
            THEME_YOUR_CUSTOM_THEME -> activity.setTheme(R.style.RedTheme)
        }
    }
}