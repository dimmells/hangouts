package com.example.dmelnyk.ft_hangouts.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.SettingManager
import com.example.dmelnyk.ft_hangouts.utils.Utils
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    private var permissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS)

    private lateinit var settingManager: SettingManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        settingManager = SettingManager(this)
        Utils.setTheme(settingManager.getTheme())
        Utils.onActivityCreateSetTheme(this)
        setContentView(R.layout.activity_main)
        checkPermission()
        countNotGranted()
        Utils.setLanguage(resources, settingManager.getLanguage())
        setFragment(ChatListFragment.newInstance(), false)
    }

    private fun checkPermission() {
        for (i in permissions) {
            if (ContextCompat.checkSelfPermission(this, i) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, 10)
                }
            }
        }
    }

    private fun countNotGranted() {
        for (i in permissions) {
            if (ContextCompat.checkSelfPermission(this, i) != PackageManager.PERMISSION_GRANTED) {
                recreate()
            }
        }

    }

    override fun onResume() {
        super.onResume()
        val time = Date().time - settingManager.getTimeOnResume()
        Toast.makeText(this, SimpleDateFormat("mm:ss", Locale.ROOT).format(time), Toast.LENGTH_SHORT).show()
    }

    override fun onPause() {
        super.onPause()
        settingManager.saveTimeOnPause(Date().time)
    }

    private fun setFragment(fragment: Fragment, isAddToBackStack: Boolean) {
        supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.coordinator_main, fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    if (isAddToBackStack) {
                        addToBackStack(null)
                    }
                    commit()
                }
    }
}
