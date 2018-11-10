package com.example.dmelnyk.ft_hangouts.ui

import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
        val contactId = arguments?.getInt(KEY_CONTACT)
        if (contactId != null) {
            contact = dbHandler?.getContactById(contactId) ?: contact
        } else {
            Toast.makeText(context, getString(R.string.chat_unexpected_error), Toast.LENGTH_SHORT).show()
        }
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
        recycle_view_chat_message_list.adapter = ChatAdapter(this, this)
    }

    private fun selector(sms: SmsEntity): Date = Date(sms.date)

    private fun loadSmsMessages() {

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
    }

    private fun createList(cursor: Cursor?, smsList: ArrayList<SmsEntity>, key: Int) {
        if (cursor != null && cursor.moveToLast()) {
            val nameID = cursor.getColumnIndex("address")
            val messageId = cursor.getColumnIndex("body")
            val dateID = cursor.getColumnIndex("date")
            val personID = cursor.getColumnIndex("person")

            do {
                if (cursor.getString(nameID) == contact.phone_number) {
                    val dateString = cursor.getString(dateID)
                    smsList.add(SmsEntity(cursor.getString(nameID), Date(dateString.toLong()).toString(), cursor.getString(messageId), key))
                }
            } while (cursor.moveToPrevious())
        }

        cursor?.close()
    }

    override fun getItemsCount(): Int = smsList.size

    override fun onBindMessageItemReceivedView(receivedViewHolder: ChatItemReceivedViewHolder, position: Int) {
        val sms = smsList[position]

        receivedViewHolder.setMessageReceived(sms.message)
//                receivedViewHolder.setTimeReceived(sms.date)
    }

    override fun onBindMessageItemSentView(receivedViewHolder: ChatItemSentViewHolder, position: Int) {
        val sms = smsList[position]

        receivedViewHolder.setMessageSent(sms.message)
//                receivedViewHolder.setTimeSent(sms.date)
    }

    override fun onMessageItemClicked(position: Int) {
        //copy msg
    }
}