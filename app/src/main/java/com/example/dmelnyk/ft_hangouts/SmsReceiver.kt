package com.example.dmelnyk.ft_hangouts

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.telephony.SmsMessage
import android.os.Build

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        val extras = intent?.extras

        if (extras != null) {
            val sms = extras.get("pdus") as Array<Any>

            for(i in sms.indices) {
                val format = extras.getString("format")


                var smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    SmsMessage.createFromPdu(sms[i] as ByteArray, format)
                } else {
                    SmsMessage.createFromPdu(sms[i] as ByteArray)
                }

                val phoneNumber = smsMessage.originatingAddress
                val messageText = smsMessage.messageBody.toString()
            }
        }
    }
}