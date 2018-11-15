package com.example.dmelnyk.ft_hangouts.recycle.chat_list

import com.example.dmelnyk.ft_hangouts.recycle.ChatListItemViewHolder
import java.util.*

interface ChatListAdapterContract {

    interface AdapterPresenter {
        fun getItemsCount(): Int
        fun onBindMessageItemView(viewHolder: ChatListItemViewHolder, position: Int)
    }

    interface MessageItemPresenter {
        fun onMessageItemClicked(position: Int)
    }

    interface MessageItemView {
        fun setName(name: String)
        fun setMessage(message: String, textColor: Int?)
        fun setTime(time: Date?)
        fun onClick(position: Int)
        fun hideBottomLine(hide: Boolean)
    }

}