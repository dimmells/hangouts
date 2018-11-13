package com.example.dmelnyk.ft_hangouts.recycle.chat

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.ui.ChatFragment

class ChatAdapter(private val adapterPresenter: ChatAdapterContract.AdapterPresenter,
                  private val chatItemPresenter: ChatAdapterContract.MessageItemPresenter): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ChatFragment.KEY_RECEIVED -> {
                ChatItemReceivedViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_received_item, parent, false), chatItemPresenter)
            }
            ChatFragment.KEY_SENT -> {
                ChatItemSentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.message_sent_item, parent, false), chatItemPresenter)
            }
            else -> throw RuntimeException("invalid view type")
        }
    }

    override fun getItemViewType(position: Int): Int = adapterPresenter.getSmsType(position)

    override fun getItemCount(): Int = adapterPresenter.getItemsCount()

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ChatItemReceivedViewHolder -> adapterPresenter.onBindMessageItemReceivedView(holder, position)
            is ChatItemSentViewHolder -> adapterPresenter.onBindMessageItemSentView(holder, position)
        }
    }
}