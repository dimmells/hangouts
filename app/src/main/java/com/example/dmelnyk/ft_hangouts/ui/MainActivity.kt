package com.example.dmelnyk.ft_hangouts.ui

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import com.example.dmelnyk.ft_hangouts.R

class MainActivity : AppCompatActivity() {

    private var permissions = arrayOf(Manifest.permission.CAMERA,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS,
            Manifest.permission.READ_SMS,
            Manifest.permission.SEND_SMS)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        for (i in permissions) {
            if (ContextCompat.checkSelfPermission(this,
                            i) != PackageManager.PERMISSION_GRANTED) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(permissions, 10)
                }
            }
        }

        setFragment(ChatListFragment.newInstance(), false)
    }


    private fun setFragment(fragment: Fragment, isAddtoBackStack: Boolean) {
        supportFragmentManager.beginTransaction()
                .apply {
                    replace(R.id.coordinator_main, fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    if (isAddtoBackStack) {
                        addToBackStack(null)
                    }
                    commit()
                }
    }
}
