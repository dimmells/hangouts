package com.example.dmelnyk.ft_hangouts.recycle.chat_list

import com.example.dmelnyk.ft_hangouts.recycle.ChatListItemViewHolder

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
        fun setMessage(message: String)
        fun setTime(time: String)
        fun onClick(position: Int)
    }

}