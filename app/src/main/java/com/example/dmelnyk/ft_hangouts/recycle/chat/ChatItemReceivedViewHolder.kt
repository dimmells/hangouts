package com.example.dmelnyk.ft_hangouts.recycle.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.message_received_item.view.*

class ChatItemReceivedViewHolder(itemView: View, private val presenter: ChatAdapterContract.MessageItemPresenter): RecyclerView.ViewHolder(itemView), ChatAdapterContract.MessageReceivedItemView {

    override fun setMessageReceived(message: String) {
        itemView.text_view_message_received.text = message
    }

    override fun setTimeReceived(time: String) {
    }
}