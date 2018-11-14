package com.example.dmelnyk.ft_hangouts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import kotlinx.android.synthetic.main.fragment_contact_info.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*

class ContactInfoFragment: Fragment() {

    companion object {
        private const val KEY_CONTACT = "contact"

        fun newInstance(contactId: Int): ContactInfoFragment {
            val fragment = ContactInfoFragment()
            val args = Bundle()
            args.putInt(KEY_CONTACT, contactId)
            fragment.arguments = args
            return fragment
        }
    }

    private lateinit var contact: Contact
    private var dbHandler: DBHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
        val contactId = arguments?.getInt(ChatFragment.KEY_CONTACT)
        if (contactId != null) {
            contact = dbHandler?.getContactById(contactId) ?: contact
        } else {
            Toast.makeText(context, getString(R.string.chat_unexpected_error), Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_contact_info, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(contact) {
            val title = "$first_name $last_name"
            toolbar_view_contact.text_view_toolbar_title.text = title
            text_view_view_contact_first_name.text = first_name
            text_view_view_contact_last_name.text = last_name
            text_view_view_contact_number.text = phone_number
            text_view_view_contact_email.text = email
            button_view_contact_delete.setOnClickListener{ dbHandler?.deleteContact(id) }
        }
        toolbar_view_contact.button_toolbar_back.setOnClickListener { fragmentManager?.popBackStack() }
        button_view_contact_edit.setOnClickListener {  }
    }
}