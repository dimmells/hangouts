package com.example.dmelnyk.ft_hangouts

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup

class MessageListAdapter(private val adapterPresenter: MessageListAdapterContract.AdapterPresenter,
                         private val messageListItemPresenter: MessageListAdapterContract.MessageItemPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
            MessageListItemViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_message_list, parent, false), messageListItemPresenter)

    override fun getItemCount(): Int = adapterPresenter.getItemsCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        adapterPresenter.onBindMessageItemView(holder as MessageListItemViewHolder, position)
    }

}