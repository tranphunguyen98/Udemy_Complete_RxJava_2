package com.tranphunguyen.contactmanager.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

import com.tranphunguyen.contactmanager.db.entity.Contact

import java.util.ArrayList


class DatabaseHelper(context: Context) : SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {

    val allContacts: ArrayList<Contact>
        get() {
            val contacts = ArrayList<Contact>()

            val selectQuery = "SELECT  * FROM " + Contact.TABLE_NAME + " ORDER BY " +
                    Contact.COLUMN_ID + " DESC"

            val db = this.writableDatabase
            val cursor = db.rawQuery(selectQuery, null)


            if (cursor.moveToFirst()) {
                do {

                    val contact = Contact(
                        cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
                        cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)),
                        cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID))
                    )

                    contacts.add(contact)
                } while (cursor.moveToNext())
            }

            db.close()

            return contacts
        }

    override fun onCreate(db: SQLiteDatabase) {

        db.execSQL(Contact.CREATE_TABLE)
    }


    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {

        db.execSQL("DROP TABLE IF EXISTS " + Contact.TABLE_NAME)

        onCreate(db)
    }

    fun insertContact(name: String, email: String): Long {

        val db = this.writableDatabase

        val values = ContentValues()


        values.put(Contact.COLUMN_NAME, name)
        values.put(Contact.COLUMN_EMAIL, email)


        val id = db.insert(Contact.TABLE_NAME, null, values)


        db.close()


        return id
    }

    fun getContact(id: Long): Contact {


        val db = this.readableDatabase

        val cursor = db.query(
            Contact.TABLE_NAME,
            arrayOf(Contact.COLUMN_ID, Contact.COLUMN_NAME, Contact.COLUMN_EMAIL),
            Contact.COLUMN_ID + "=?",
            arrayOf(id.toString()), null, null, null, null
        )

        cursor?.moveToFirst()


        val contact = Contact(
            cursor.getString(cursor.getColumnIndex(Contact.COLUMN_NAME)),
            cursor.getString(cursor.getColumnIndex(Contact.COLUMN_EMAIL)),
            cursor.getInt(cursor.getColumnIndex(Contact.COLUMN_ID))
        )


        cursor!!.close()

        return contact
    }


    fun updateContact(contact: Contact): Int {
        val db = this.writableDatabase

        val values = ContentValues()
        values.put(Contact.COLUMN_NAME, contact.name)
        values.put(Contact.COLUMN_EMAIL, contact.email)


        return db.update(
            Contact.TABLE_NAME, values, Contact.COLUMN_ID + " = ?",
            arrayOf(contact.id.toString())
        )
    }

    fun deleteContact(contact: Contact) {
        val db = this.writableDatabase
        db.delete(
            Contact.TABLE_NAME, Contact.COLUMN_ID + " = ?",
            arrayOf(contact.id.toString())
        )
        db.close()
    }

    companion object {

        private val DATABASE_VERSION = 1


        private val DATABASE_NAME = "contact_db"
    }
}
