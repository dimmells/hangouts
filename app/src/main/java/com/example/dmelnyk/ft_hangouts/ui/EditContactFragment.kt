package com.example.dmelnyk.ft_hangouts.ui

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import com.example.dmelnyk.ft_hangouts.data.Contact
import com.example.dmelnyk.ft_hangouts.data.DBHandler
import com.example.dmelnyk.ft_hangouts.data.PhotoFormatter
import kotlinx.android.synthetic.main.fragment_create_contact.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import java.io.IOException

class EditContactFragment: Fragment() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_REQUEST = 2
        private const val KEY_CONTACT = "contact"

        fun newInstance(contactId: Int): EditContactFragment {
            val fragment = EditContactFragment()
            val args = Bundle()
            args.putInt(KEY_CONTACT, contactId)
            fragment.arguments = args
            return fragment
        }
    }

    private var dbHandler: DBHandler? = null
    private val photoFormatter = PhotoFormatter()
    private lateinit var photoSrc: String
    private lateinit var contact: Contact

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dbHandler = this.context?.let { DBHandler(it) }
        val contactId = arguments?.getInt(ChatFragment.KEY_CONTACT)
        if (contactId != null) {
            contact = dbHandler?.getContactById(contactId) ?: contact
            photoSrc = contact.photoSrc
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
        image_view_add_contact_photo.setImageBitmap(activity?.let { photoFormatter.getPhoto(contact.photoSrc, it) })
        image_view_add_contact_photo.setOnClickListener { loadPhoto() }
        button_add_contact_save.text = getString(R.string.edit_contact_button_edit)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.data != null) {

            val uri = data.data

            try {
                val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
                val formattedPhoto = photoFormatter.rotateAndCrop(bitmap, 90f)
                val src = context?.let { photoFormatter.savePhoto(formattedPhoto, it, resources) }
                if (src != null) photoSrc = src
                image_view_add_contact_photo.setImageBitmap(formattedPhoto)
            } catch (e: IOException) { }
        }

        if (requestCode == CAMERA_REQUEST && resultCode == Activity.RESULT_OK) {

            val photo = data?.extras?.get("data") as Bitmap
            val formattedPhoto = photoFormatter.rotateAndCrop(photo, 0f)
            val src = context?.let { photoFormatter.savePhoto(formattedPhoto, it, resources) }
            if (src != null) photoSrc = src
            image_view_add_contact_photo.setImageBitmap(formattedPhoto)
        }
    }

    private fun loadPhoto() {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder
                .setMessage("Choose photo source")
                .setPositiveButton("Camera", { _, _ ->
                    if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
                    } else {
                        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    }
                })
                .setNegativeButton("Gallery", {_, _ ->
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST)
                })
        alertDialogBuilder.create().show()
    }

    private fun edit() {
        if (text_view_add_contact_number.text.isNotEmpty()) {
            val contact = Contact(
                    contact.id,
                    text_view_add_contact_first_name.text.toString(),
                    text_view_add_contact_last_name.text.toString(),
                    text_view_add_contact_number.text.toString(),
                    text_view_add_contact_email.text.toString(),
                    photoSrc,
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