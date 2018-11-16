package com.example.dmelnyk.ft_hangouts.recycle

import android.app.Activity
import android.support.v7.widget.RecyclerView
import android.view.View
import com.example.dmelnyk.ft_hangouts.recycle.chat_list.ChatListAdapterContract
import kotlinx.android.synthetic.main.item_message_list.view.*
import java.text.SimpleDateFormat
import java.util.*
import com.example.dmelnyk.ft_hangouts.data.PhotoFormatter

class ChatListItemViewHolder(itemView: View, private val presenter: ChatListAdapterContract.MessageItemPresenter): RecyclerView.ViewHolder(itemView), ChatListAdapterContract.MessageItemView {

    override fun setName(name: String) { itemView.text_view_message_list_name.text = name }

    override fun setMessage(message: String, textColor: Int?) {
        itemView.text_view_message_list_message.text = message
        if (textColor != null) {
            itemView.text_view_message_list_message.setTextColor(textColor)
        }
    }

    override fun setTime(time: Date?) {
        if (time != null) {
            itemView.text_view_message_list_time.text = SimpleDateFormat("HH:mm", Locale.ROOT).format(time)
        } else {
            itemView.text_view_message_list_time.text = ""
        }
    }

    override fun onClick(position: Int) { itemView.setOnClickListener { presenter.onMessageItemClicked(position) } }

    override fun hideBottomLine(hide: Boolean) { itemView.view_item_message_list_bottom_line.visibility = if (hide) View.INVISIBLE else View.VISIBLE }

    override fun setPhoto(photoSrc: String, activity: Activity) {
        if (photoSrc != "default") {
            val bitmap = PhotoFormatter().getPhoto(photoSrc, activity)
            if (bitmap != null) {
                itemView.image_view_message_list_photo.setImageBitmap(bitmap)
            }
        }
    }
}