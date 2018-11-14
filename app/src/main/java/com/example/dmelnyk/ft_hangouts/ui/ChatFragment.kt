package com.example.dmelnyk.ft_hangouts.ui

import android.content.BroadcastReceiver
import android.content.Context
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
import android.content.Intent
import android.content.IntentFilter
import android.support.v4.app.FragmentTransaction
import com.example.dmelnyk.ft_hangouts.data.SmsLoader
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
    private var smsLoader: SmsLoader? = null
    private val smsList = ArrayList<SmsEntity>()
    private val smsAdapter = ChatAdapter(this, this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
        smsLoader = activity?.let { SmsLoader(it) }
        val contactId = arguments?.getInt(KEY_CONTACT)
        if (contactId != null) {
            contact = dbHandler?.getContactById(contactId) ?: contact
        } else {
            Toast.makeText(context, getString(R.string.chat_unexpected_error), Toast.LENGTH_SHORT).show()
        }

        try {
            val broadcastReceiver = object : BroadcastReceiver() {
                override fun onReceive(context: Context, intent: Intent) {
                    smsList.clear()
                    smsLoader?.getContactSmsList(contact)?.let { smsList.addAll(it) }
                    recycle_view_chat_message_list.adapter = smsAdapter
                    recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
                }
            }
            val intentFilter = IntentFilter("android.provider.Telephony.SMS_RECEIVED")
            activity?.registerReceiver(broadcastReceiver, intentFilter)
        } catch (e: Exception) {
            Toast.makeText(context, e.localizedMessage, Toast.LENGTH_LONG).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_chat, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        smsList.clear()
        smsLoader?.getContactSmsList(contact)?.let { smsList.addAll(it) }
        val title = "${contact.first_name} ${contact.last_name}"
        toolbar_chat.text_view_toolbar_title.text = title
        toolbar_chat.button_toolbar_back.setOnClickListener{ fragmentManager?.popBackStack() }
        toolbar_chat.image_view_toolbar_info.setOnClickListener { setFragment(ContactInfoFragment.newInstance(contact.id)) }
        toolbar_chat.image_view_toolbar_info.visibility = View.VISIBLE
        recycle_view_chat_message_list.layoutManager = LinearLayoutManager(context)
        recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
        image_button_chat_send.setOnClickListener { onSendButtonClick() }
        edit_text_chat_message.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        recycle_view_chat_message_list.adapter = smsAdapter
        recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
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
                            smsList.clear()
                            smsLoader?.getContactSmsList(contact)?.let { smsList.addAll(it) }
                            recycle_view_chat_message_list.adapter = smsAdapter
                            recycle_view_chat_message_list.scrollToPosition(smsList.lastIndex)
                        }, 1000)
            } catch (e: Exception) {
                Toast.makeText(context, e.message.toString(), Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun setFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.apply {
                    replace(R.id.coordinator_main, fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    addToBackStack(null)
                    commit()
                }
    }
}