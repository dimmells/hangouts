package com.example.dmelnyk.ft_hangouts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.FragmentTransaction
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.dmelnyk.ft_hangouts.recycle.chat_list.ChatListAdapter
import com.example.dmelnyk.ft_hangouts.recycle.chat_list.ChatListAdapterContract
import com.example.dmelnyk.ft_hangouts.recycle.ChatListItemViewHolder
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import com.example.dmelnyk.ft_hangouts.data.SmsLoader
import kotlinx.android.synthetic.main.fragment_message_list.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*
import android.content.Intent
import android.support.v4.content.ContextCompat.startActivity
import android.app.Activity
import com.example.dmelnyk.ft_hangouts.utils.Utils


class ChatListFragment: Fragment(), ChatListAdapterContract.AdapterPresenter, ChatListAdapterContract.MessageItemPresenter {

    companion object {

        fun newInstance(): ChatListFragment = ChatListFragment()
    }

    private val chatList: MutableList<Contact> = ArrayList()
    private var dbHandler: DBHandler? = null
    private var smsLoader: SmsLoader? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        smsLoader = activity?.let { SmsLoader(it) }
        dbHandler = this.context?.let { DBHandler(it) }
        dbHandler?.getAllContact()?.let { chatList.addAll(it) }
        loadLastMessage()
    }

    override fun onResume() {
        super.onResume()
        chatList.removeAll(chatList)
        dbHandler?.getAllContact()?.let { chatList.addAll(it) }
        loadLastMessage()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_message_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle_view_message_list.layoutManager = LinearLayoutManager(context)
        recycle_view_message_list.adapter = ChatListAdapter(this, this)
        toolbar_chat.text_view_toolbar_title.text = getString(R.string.app_name)
        toolbar_chat.button_toolbar_back.visibility = View.GONE
        fab.setOnClickListener { setFragment(CreateContactFragment.newInstance()) }
        button_app.setOnClickListener {
            activity?.let { it1 -> Utils.changeToTheme(it1, Utils.THEME_MATERIAL_LIGHT) }
        }
        button_red.setOnClickListener {
            activity?.let { it1 -> Utils.changeToTheme(it1, Utils.THEME_YOUR_CUSTOM_THEME) }
        }
    }

    override fun getItemsCount(): Int = chatList.size

    override fun onBindMessageItemView(viewHolder: ChatListItemViewHolder, position: Int) {
        val contact = chatList[position]

        viewHolder.onClick(position)
        viewHolder.setName("${contact.first_name} ${contact.last_name}")
        val lastMessage = contact.lastMessage
        if (lastMessage != null) {
            viewHolder.setMessage(lastMessage.message, resources.getColor(R.color.colorMessageText))
            viewHolder.setTime(lastMessage.date)
        } else {
            viewHolder.setMessage(getString(R.string.chat_list_item_no_message), resources.getColor(R.color.colorPrimary))
            viewHolder.setTime(null)
        }
        viewHolder.hideBottomLine(position == chatList.size - 1)
    }

    override fun onMessageItemClicked(position: Int) { setFragment(ChatFragment.newInstance(chatList[position].id))}


    private fun setFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
                ?.apply {
                    replace(R.id.coordinator_main, fragment)
                    setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    addToBackStack(null)
                    commit()
                }
    }

    private fun loadLastMessage() = chatList.forEach { it.lastMessage = smsLoader?.getContactLastMessage(it) }

    fun changeToTheme(activity: Activity, theme: Int) {
		val sTheme = theme
		activity.finish()
		activity.startActivity(Intent(activity, activity::class.java))
		activity.overridePendingTransition(android.R.anim.fade_in,
				android.R.anim.fade_out)
	}

}