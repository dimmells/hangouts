package com.example.dmelnyk.ft_hangouts.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast

class SmsReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        try {
            val extras = intent.extras

            if (extras != null) {
                val sms = extras.get("pdus") as Array<*>

                for (i in sms.indices) {
//                    val format = extras.getString("format")


//                    var smsMessage = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                        SmsMessage.createFromPdu(sms[i] as ByteArray, format)
//                    } else {
//                        SmsMessage.createFromPdu(sms[i] as ByteArray)
//                    }
//
//                    val phoneNumber = smsMessage.originatingAddress
//                    val messageText = smsMessage.messageBody.toString()
                }
            }
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }
}
