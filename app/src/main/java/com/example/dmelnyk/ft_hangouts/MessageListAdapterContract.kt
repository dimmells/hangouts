package com.example.dmelnyk.ft_hangouts

interface MessageListAdapterContract {

    interface AdapterPresenter {
        fun getItemsCount(): Int
        fun onBindMessageItemView(viewHolder: MessageListItemViewHolder, position: Int)
    }

    interface MessageItemPresenter {
        fun onMessageItemClicked(position: Int)
    }

    interface MessageItemView {
        fun setName(name: String)
        fun setMessage(message: String)
        fun setTime(time: String)
    }

}