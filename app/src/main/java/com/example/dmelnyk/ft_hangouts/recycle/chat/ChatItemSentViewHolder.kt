package com.example.dmelnyk.ft_hangouts.recycle.chat

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.message_sent_item.view.*

class ChatItemSentViewHolder(itemView: View, private val presenter: ChatAdapterContract.MessageItemPresenter): RecyclerView.ViewHolder(itemView), ChatAdapterContract.MessageSentItemView {

    override fun setMessageSent(message: String) {
        itemView.text_view_message_sent.text = message
    }

    override fun setTimeSent(time: String) {
    }
}