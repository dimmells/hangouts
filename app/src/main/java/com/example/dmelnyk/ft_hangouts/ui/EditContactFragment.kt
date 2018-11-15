package com.example.dmelnyk.ft_hangouts.ui

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import kotlinx.android.synthetic.main.fragment_create_contact.*
import kotlinx.android.synthetic.main.fragment_toolbar.*

class EditContactFragment: Fragment() {

    companion object {

        const val KEY_CONTACT = "contact"

        fun newInstance(contactId: Int): EditContactFragment {
            val fragment = EditContactFragment()
            val args = Bundle()
            args.putInt(KEY_CONTACT, contactId)
            fragment.arguments = args
            return fragment
        }
    }

    private var dbHandler: DBHandler? = null
    private lateinit var contact: Contact

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
            inflater.inflate(R.layout.fragment_create_contact, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_view_toolbar_title.text = getString(R.string.edit_contact_toolbar_title)
        button_toolbar_back.setOnClickListener { fragmentManager?.popBackStack() }
        button_add_contact_save.setOnClickListener { edit() }
        with (contact) {
            if (first_name != phone_number) {
                text_view_add_contact_first_name.setText(first_name, TextView.BufferType.EDITABLE)
            }
            text_view_add_contact_last_name.setText(last_name, TextView.BufferType.EDITABLE)
            text_view_add_contact_number.setText(phone_number, TextView.BufferType.EDITABLE)
            text_view_add_contact_email.setText(email, TextView.BufferType.EDITABLE)
        }
        button_add_contact_save.text = getString(R.string.edit_contact_button_edit)
    }

    private fun edit() {
        if (text_view_add_contact_number.text.isNotEmpty()) {
            val contact = Contact(
                    contact.id,
                    text_view_add_contact_first_name.text.toString(),
                    text_view_add_contact_last_name.text.toString(),
                    text_view_add_contact_number.text.toString(),
                    text_view_add_contact_email.text.toString(),
                    "default",
                    null
            )
            if (contact.first_name.isEmpty() && contact.last_name.isEmpty()) {
                contact.first_name = contact.phone_number
            }
            val isEdit = dbHandler?.updateContact(contact)
            if (isEdit != null && isEdit) {
                fragmentManager?.popBackStack()
            } else {
                Toast.makeText(context, context?.getString(R.string.db_error), Toast.LENGTH_LONG).show()
            }
        } else {
            Toast.makeText(context, getString(R.string.add_contact_no_number_alert), Toast.LENGTH_SHORT).show()
        }
    }
}