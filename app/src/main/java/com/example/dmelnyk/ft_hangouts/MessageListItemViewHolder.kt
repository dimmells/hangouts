package com.example.dmelnyk.ft_hangouts

import android.support.v7.widget.RecyclerView
import android.view.View
import kotlinx.android.synthetic.main.item_message_list.view.*

class MessageListItemViewHolder(itemView: View, presenter: MessageListAdapterContract.MessageItemPresenter): RecyclerView.ViewHolder(itemView), MessageListAdapterContract.MessageItemView {

    init {

    }

    override fun setName(name: String) { itemView.text_view_message_list_name.text = name }

    override fun setMessage(message: String) { itemView.text_view_message_list_message.text = message }

    override fun setTime(time: String) { itemView.text_view_message_list_time.text = time }
}