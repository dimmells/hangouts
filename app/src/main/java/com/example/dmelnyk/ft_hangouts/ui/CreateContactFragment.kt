package com.example.dmelnyk.ft_hangouts.ui

import android.Manifest
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
import android.content.Intent
import android.provider.MediaStore
import android.app.Activity.RESULT_OK
import java.io.IOException
import com.example.dmelnyk.ft_hangouts.data.PhotoFormatter
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.graphics.Bitmap
import android.app.Activity
import android.app.AlertDialog
import android.text.InputType

class CreateContactFragment: Fragment() {

    companion object {
        const val PICK_IMAGE_REQUEST = 1
        const val CAMERA_REQUEST = 2
        const val IMAGE_DIRECTORY = "/ft_hangouts_image"

        fun newInstance(): CreateContactFragment = CreateContactFragment()
    }

    private var dbHandler: DBHandler? = null
    private val photoFormatter = PhotoFormatter()
    private var photoSrc = "default"

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
        image_view_add_contact_photo.setOnClickListener { loadPhoto() }
        text_view_add_contact_first_name.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
        text_view_add_contact_last_name.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_FLAG_CAP_SENTENCES
    }

    private fun loadPhoto() {
        val alertDialogBuilder = AlertDialog.Builder(activity)
        alertDialogBuilder
                .setMessage(getString(R.string.alert_choose_ph_src))
                .setPositiveButton(getString(R.string.alert_src_camera), { _, _ ->
                    if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } != PackageManager.PERMISSION_GRANTED) {
                        requestPermissions(arrayOf(Manifest.permission.CAMERA), 10)
                    } else {
                        val cameraIntent = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
                        startActivityForResult(cameraIntent, CAMERA_REQUEST)
                    }
                })
                .setNegativeButton(getString(R.string.alert_src_gallery), { _, _ ->
                    val intent = Intent()
                    intent.type = "image/*"
                    intent.action = Intent.ACTION_GET_CONTENT
                    startActivityForResult(Intent.createChooser(intent, getString(R.string.create_contact_select_pic)), PICK_IMAGE_REQUEST)
                })
        alertDialogBuilder.create().show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {

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

    private fun save() {
        if (text_view_add_contact_number.text.isNotEmpty()) {
            val contact = Contact(
                    0,
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
            val isSave = dbHandler?.addContactToDb(contact)
            if (isSave != null && isSave) {
                fragmentManager?.popBackStack()
            } else {
                Toast.makeText(context, getString(R.string.create_contact_check_and_try), Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(context, getString(R.string.add_contact_no_number_alert), Toast.LENGTH_SHORT).show()
        }
    }

}