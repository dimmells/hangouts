package com.example.dmelnyk.ft_hangouts.data

import android.app.Activity
import android.database.Cursor
import android.net.Uri
import android.support.v4.app.FragmentActivity
import com.example.dmelnyk.ft_hangouts.ui.ChatFragment
import java.util.*
import kotlin.collections.ArrayList

class SmsLoader(private val activity: FragmentActivity) {

    private val smsList = ArrayList<SmsEntity>()

    fun getContactSmsList(contact: Contact): ArrayList<SmsEntity> {
        loadSmsMessages(contact)
        return smsList
    }

    fun getContactLastMessage(contact: Contact): SmsEntity? {
        loadSmsMessages(contact)
        return try {
            smsList.last()
        } catch (e: Exception) {
            null
        }
    }

    private fun selector(sms: SmsEntity): Date = sms.date

    private fun loadSmsMessages(contact: Contact) {
        smsList.clear()
        val cursor = activity.contentResolver?.query(
                Uri.parse("content://sms/inbox"),
                null,
                null,
                null,
                null
        )
        createList(cursor, smsList, ChatFragment.KEY_RECEIVED,contact)
        val cursorSent = activity.contentResolver?.query(
                Uri.parse("content://sms/sent"),
                null,
                null,
                null,
                null
        )
        createList(cursorSent, smsList, ChatFragment.KEY_SENT, contact)

        smsList.sortBy({selector(it)})
    }

    private fun createList(cursor: Cursor?, smsList: ArrayList<SmsEntity>, key: Int, contact: Contact) {
        if (cursor != null && cursor.moveToLast()) {
            val nameID = cursor.getColumnIndex("address")
            val messageId = cursor.getColumnIndex("body")
            val dateID = cursor.getColumnIndex("date")

            do {
                if (cursor.getString(nameID) == contact.phone_number) {
                    val dateString = cursor.getString(dateID)

                    smsList.add(SmsEntity(cursor.getString(nameID), Date(dateString.toLong()), cursor.getString(messageId), key))
                }
            } while (cursor.moveToPrevious())
        }

        cursor?.close()
    }
}