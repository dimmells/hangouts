package com.example.dmelnyk.ft_hangouts.recycle

import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.dmelnyk.ft_hangouts.recycle.chat_list.ChatListAdapterContract
import kotlinx.android.synthetic.main.item_message_list.view.*

class ChatListItemViewHolder(itemView: View, private val presenter: ChatListAdapterContract.MessageItemPresenter): RecyclerView.ViewHolder(itemView), ChatListAdapterContract.MessageItemView {

    override fun setName(name: String) { itemView.text_view_message_list_name.text = name }

    override fun setMessage(message: String) { itemView.text_view_message_list_message.text = message }

    override fun setTime(time: String) { itemView.text_view_message_list_time.text = time }

    override fun onClick(position: Int) { itemView.setOnClickListener { presenter.onMessageItemClicked(position) } }
}