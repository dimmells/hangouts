package com.example.dmelnyk.ft_hangouts.recycle.chat_list

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.recycle.ChatListItemViewHolder

class ChatListAdapter(private val adapterPresenter: ChatListAdapterContract.AdapterPresenter,
                      private val chatListItemPresenter: ChatListAdapterContract.MessageItemPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            ChatListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message_list, parent, false), chatListItemPresenter)

    override fun getItemCount(): Int = adapterPresenter.getItemsCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterPresenter.onBindMessageItemView(holder as ChatListItemViewHolder, position)
    }

}