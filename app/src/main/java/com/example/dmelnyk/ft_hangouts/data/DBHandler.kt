package com.example.dmelnyk.ft_hangouts.data

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.util.Log
import android.widget.Toast
import com.example.dmelnyk.ft_hangouts.R
import java.io.Serializable

class DBHandler(private var context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, 1), Serializable {

    companion object {
        const val DATABASE_NAME = "PhoneBookDB"
        const val TABLE_NAME = "Contacts"
        const val COL_ID = "id"
        const val COL_FIRST_NAME = "firstName"
        const val COL_LAST_NAME = "lastName"
        const val COL_PHONE_NUMBER = "phoneNumber"
        const val COL_EMAIL = "email"
        const val COL_PHOTO = "photo"
    }

    override fun onCreate(p0: SQLiteDatabase?) {
        val createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COL_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COL_FIRST_NAME + " VARCHAR(256)," +
                COL_LAST_NAME + " VARCHAR(256)," +
                COL_PHONE_NUMBER + " VARCHAR(256)," +
                COL_EMAIL + " VARCHAR(256)," +
                COL_PHOTO + " VARCHAR(256))"
        p0?.execSQL(createTable)
    }

    override fun onUpgrade(p0: SQLiteDatabase?, p1: Int, p2: Int) {}

    fun addContactToDb(contact: Contact): Boolean {
        if (!isUnique(contact.phone_number)) {
            return false
        }
        val cv = ContentValues()
        with(cv) {
            put(COL_FIRST_NAME, contact.first_name)
            put(COL_LAST_NAME, contact.last_name)
            put(COL_PHONE_NUMBER, contact.phone_number)
            put(COL_EMAIL, contact.email)
            put(COL_PHOTO, contact.photoSrc)
        }
        val result = writableDatabase.insert(TABLE_NAME, null, cv)
        return if (result == (-1).toLong()) {
            Toast.makeText(context, context.getString(R.string.db_failed), Toast.LENGTH_SHORT).show()
            false
        }
        else {
            Toast.makeText(context, context.getString(R.string.db_success), Toast.LENGTH_SHORT).show()
            true
        }
    }

    fun getAllContact() : MutableList<Contact> {
        val list: MutableList<Contact> = ArrayList()

        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                val contact = Contact(
                        result.getInt(result.getColumnIndex(COL_ID)),
                        result.getString(result.getColumnIndex(COL_FIRST_NAME)),
                        result.getString(result.getColumnIndex(COL_LAST_NAME)),
                        result.getString(result.getColumnIndex(COL_PHONE_NUMBER)),
                        result.getString(result.getColumnIndex(COL_EMAIL)),
                        result.getString(result.getColumnIndex(COL_PHOTO)),
                        null)
                list.add(contact)
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return list
    }

    fun getContactById(contactId: Int): Contact {
        val contact = Contact()
        val db = readableDatabase
        val query = "SELECT * FROM $TABLE_NAME WHERE $COL_ID = \"$contactId\""
        val cursor = db.rawQuery(query, null)

        if (cursor.moveToFirst()) {
            cursor.moveToFirst()
            with(contact) {
                id = cursor.getInt(cursor.getColumnIndex(COL_ID))
                first_name = cursor.getString(cursor.getColumnIndex(COL_FIRST_NAME))
                last_name = cursor.getString(cursor.getColumnIndex(COL_LAST_NAME))
                phone_number = cursor.getString(cursor.getColumnIndex(COL_PHONE_NUMBER))
                email = cursor.getString(cursor.getColumnIndex(COL_EMAIL))
                photoSrc = cursor.getString(cursor.getColumnIndex(COL_PHOTO))
            }
        }

        return contact
    }

    private fun isUnique(phoneNumber: String): Boolean {
        val db = this.readableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)

        if (result.moveToFirst()) {
            do {
                if (result.getString(result.getColumnIndex(COL_PHONE_NUMBER)) == phoneNumber) {
                    return false
                }
            } while (result.moveToNext())
        }

        result.close()
        db.close()

        return true
    }

    fun updateContact(contactToUpdate: Contact): Boolean {
        val db = writableDatabase
        val query = "SELECT * FROM $TABLE_NAME"
        val result = db.rawQuery(query, null)

        val cv = ContentValues()
        with(cv) {
            put(COL_FIRST_NAME, contactToUpdate.first_name)
            put(COL_LAST_NAME, contactToUpdate.last_name)
            put(COL_PHONE_NUMBER, contactToUpdate.phone_number)
            put(COL_EMAIL, contactToUpdate.email)
            put(COL_PHOTO, contactToUpdate.photoSrc)
        }
        return try {
            db.update(TABLE_NAME, cv, "$COL_ID=?", arrayOf(contactToUpdate.id.toString()))
            result.close()
            db.close()
            true
        } catch (e: Exception) {
            false
        }
    }

    fun deleteContact(id: Int) {
        writableDatabase.delete(TABLE_NAME, "$COL_ID=?", arrayOf(id.toString()))
        writableDatabase.close()
    }
}