package com.example.dmelnyk.ft_hangouts

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_message_list.*

class MessageListFragment: Fragment(), MessageListAdapterContract.AdapterPresenter, MessageListAdapterContract.MessageItemPresenter {

    companion object {
        const val KEY_DB = "db"

        fun newInstance(dbHandler: DBHandler): MessageListFragment {
            val fragment = MessageListFragment()
            val args = Bundle()
            args.putSerializable(KEY_DB, dbHandler)
            fragment.arguments = args
            return fragment
        }
    }

    private val chatList: MutableList<Contact> = ArrayList()
    private lateinit var dbHandler: DBHandler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = arguments?.getSerializable(KEY_DB) as DBHandler
        chatList.addAll(dbHandler.getAllContact())
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_message_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recycle_view_message_list.layoutManager = LinearLayoutManager(context)
        recycle_view_message_list.adapter = MessageListAdapter(this, this)
    }

    override fun getItemsCount(): Int = chatList.size

    override fun onBindMessageItemView(viewHolder: MessageListItemViewHolder, position: Int) {
        val contact = chatList[position]
        viewHolder.setName("${contact.first_name} ${contact.last_name}")
//        viewHolder.setMessage()
//        viewHolder.setTime()
    }

    override fun onMessageItemClicked(position: Int) {}
}