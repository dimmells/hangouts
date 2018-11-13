package com.example.dmelnyk.ft_hangouts.ui

import android.content.BroadcastReceiver
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import com.example.dmelnyk.ft_hangouts.data.SmsEntity
import com.example.dmelnyk.ft_hangouts.recycle.chat.ChatAdapter
import com.example.dmelnyk.ft_hangouts.recycle.chat.ChatAdapterContract
import com.example.dmelnyk.ft_hangouts.recycle.chat.ChatItemReceivedViewHolder
import com.example.dmelnyk.ft_hangouts.recycle.chat.ChatItemSentViewHolder
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*
import java.util.*
import android.text.InputType
import com.example.dmelnyk.ft_hangouts.SmsReceiver
import android.content.Intent
import android.content.IntentFilter
import java.text.SimpleDateFormat

class ChatFragment: Fragment(), ChatAdapterContract.AdapterPresenter, ChatAdapterContract.MessageItemPresenter {

    companion object {
        const val KEY_CONTACT = "contact"
        const val KEY_RECEIVED = 1
        const val KEY_SENT = 2

        fun newInstance(contactId: Int): ChatFragment {
            val fragment = ChatFragment()
            val args = Bundle()
            args.putInt(KEY_CONTACT, contactId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var contact: Contact
    private var dbHandler: DBHandler? = null
    private val smsList = ArrayList<SmsEntity>()
    private val smsAdapter = ChatAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
        val contactId = arguments?.getInt(KEY_CONTACT)
        if (contactId != null) {
            contact = dbHandler?.getContactById(contactId) ?: contact
        } else {
            Toast.makeText(context, getString(R.string.chat_unexpected_error), Toast.LENGTH_SHORT).show()
        }

        val br = object : BroadcastReceiver() {
            // действия при получении сообщений
            override fun onReceive(context: Context, intent: Intent) {
                loadSmsMessages()
            }
        }
        val intFilt = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
        activity?.registerReceiver(br, intFilt)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        loadSmsMessages()
        val title = "${contact.first_name} ${contact.last_name}"
        toolbar_chat.text_view_toolbar_title.text = title
        toolbar_chat.button_toolbar_back.setOnClickListener{ fragmentManager?.popBackStack() }
        recycle_view_chat_message_list.layoutManager = LinearLayoutManager(context)
        recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
        image_button_chat_send.setOnClickListener { onSendButtonClick() }
        edit_text_chat_message.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    }

    private fun selector(sms: SmsEntity): Date = sms.date

    private fun loadSmsMessages() {
        smsList.clear()
        val cursor = activity?.contentResolver?.query(
                Uri.parse("content://sms/inbox"),
                null,
                null,
                null,
                null
        )
        createList(cursor, smsList, KEY_RECEIVED)
        val cursorSent = activity?.contentResolver?.query(
                Uri.parse("content://sms/sent"),
                null,
                null,
                null,
                null
        )
        createList(cursorSent, smsList, KEY_SENT)

        smsList.sortBy({selector(it)})
        recycle_view_chat_message_list.adapter = smsAdapter
        recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
    }

    private fun createList(cursor: Cursor?, smsList: ArrayList<SmsEntity>, key: Int) {
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

    override fun getItemsCount(): Int = smsList.size

    override fun getSmsType(position: Int): Int = smsList[position].key

    override fun onBindMessageItemReceivedView(receivedViewHolder: ChatItemReceivedViewHolder, position: Int) {
        val sms = smsList[position]

        receivedViewHolder.setMessageReceived(sms.message)
        receivedViewHolder.setTimeReceived(SimpleDateFormat("HH:mm", Locale.ROOT).format(sms.date))
    }

    override fun onBindMessageItemSentView(receivedViewHolder: ChatItemSentViewHolder, position: Int) {
        val sms = smsList[position]

        receivedViewHolder.setMessageSent(sms.message)
        receivedViewHolder.setTimeSent(SimpleDateFormat("HH:mm", Locale.ROOT).format(sms.date))
    }

    override fun onMessageItemClicked(position: Int) {
        //copy msg
    }

    private fun onSendButtonClick() {
        val message = edit_text_chat_message.text.toString()

        if (message.isNotEmpty()) {
            try {
                SmsManager.getDefault().sendTextMessage(contact.phone_number, null, message, null, null)
                edit_text_chat_message.setText("")
                Toast.makeText(context, "Sending", Toast.LENGTH_SHORT).show()
                Handler().postDelayed(
                        {
                            Toast.makeText(context, "SMS sent", Toast.LENGTH_SHORT).show()
                            loadSmsMessages()
                        }, 1000)
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }
}