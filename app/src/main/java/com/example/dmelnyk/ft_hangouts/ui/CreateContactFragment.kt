package com.example.dmelnyk.ft_hangouts.ui

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import kotlinx.android.synthetic.main.fragment_create_contact.*
import kotlinx.android.synthetic.main.fragment_toolbar.*

class CreateContactFragment: Fragment() {

    companion object {

        fun newInstance(): CreateContactFragment = CreateContactFragment()
    }

    private var dbHandler: DBHandler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_create_contact, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        text_view_toolbar_title.text = getString(R.string.add_contact_title)
        button_toolbar_back.setOnClickListener { fragmentManager?.popBackStack() }
        button_add_contact_save.setOnClickListener { save() }
    }

    private fun save() {
        if (text_view_add_contact_number.text.isNotEmpty()) {
            val contact = Contact(
                    0,
                    text_view_add_contact_first_name.text.toString(),
                    text_view_add_contact_last_name.text.toString(),
                    text_view_add_contact_number.text.toString(),
                    text_view_add_contact_email.text.toString(),
                    "default"
            )
            if (contact.first_name.isEmpty() && contact.last_name.isEmpty()) {
                contact.first_name = contact.phone_number
            }
            val isSave = dbHandler?.addContactToDb(contact)
            if (isSave != null && isSave) {
                fragmentManager?.popBackStack()
            }
        } else {
            Toast.makeText(context, getString(R.string.add_contact_no_number_alert), Toast.LENGTH_SHORT).show()
        }
    }

}