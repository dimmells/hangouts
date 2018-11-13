package com.example.dmelnyk.ft_hangouts.recycle.chat


interface ChatAdapterContract {

    interface AdapterPresenter {
        fun getItemsCount(): Int
        fun onBindMessageItemReceivedView(receivedViewHolder: ChatItemReceivedViewHolder, position: Int)
        fun onBindMessageItemSentView(receivedViewHolder: ChatItemSentViewHolder, position: Int)
        fun getSmsType(position: Int): Int
    }

    interface MessageItemPresenter {
        fun onMessageItemClicked(position: Int)
    }

    interface MessageSentItemView {
        fun setMessageSent(message: String)
        fun setTimeSent(time: String)
    }

    interface MessageReceivedItemView {
        fun setMessageReceived(message: String)
        fun setTimeReceived(time: String)
    }

}