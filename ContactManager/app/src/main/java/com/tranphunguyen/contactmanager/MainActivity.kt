package com.tranphunguyen.contactmanager

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room

import com.tranphunguyen.contactmanager.adapter.ContactsAdapter
import com.tranphunguyen.contactmanager.db.entity.Contact
import com.tranphunguyen.contactmanager.db.ContactsAppDatabase
import kotlinx.android.synthetic.main.activity_main111111111111.*

import java.util.ArrayList

class MainActivity : AppCompatActivity() {

    private var contactsAdapter: ContactsAdapter? = null
    private val contactArrayList = ArrayList<Contact>()
    private lateinit var recyclerView: RecyclerView

    private lateinit var contactAppDatabase: ContactsAppDatabase
//    private lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Contacts Manager"

        recyclerView = findViewById(R.id.recycler_view_contacts)

        contactAppDatabase = Room.databaseBuilder(
            applicationContext,
            ContactsAppDatabase::class.java,
            "ContactDB"
        ).allowMainThreadQueries().build()

//        db = DatabaseHelper(this)

//        contactArrayList.addAll(db.allContacts)
        contactArrayList.addAll(contactAppDatabase.getContactDAO().getContacts())

        contactsAdapter = ContactsAdapter(contactArrayList)
        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = contactsAdapter

        fab.setOnClickListener { addAndEditContacts(false, null, -1) }
    }

    override
    fun onCreateOptionsMenu(menu: Menu): Boolean {

        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override
    fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId

        return if (id == R.id.action_settings) {
            true
        } else super.onOptionsItemSelected(item)

    }


    fun addAndEditContacts(isUpdate: Boolean, contact: Contact?, position: Int) {
        val layoutInflaterAndroid = LayoutInflater.from(applicationContext)
        val view = layoutInflaterAndroid.inflate(R.layout.layout_add_contact, null)

        val alertDialogBuilderUserInput = AlertDialog.Builder(this@MainActivity)
        alertDialogBuilderUserInput.setView(view)

        val contactTitle = view.findViewById<TextView>(R.id.new_contact_title)
        val newContact = view.findViewById<EditText>(R.id.name)
        val contactEmail = view.findViewById<EditText>(R.id.email)

        contactTitle.text = if (!isUpdate) "Add New Contact" else "Edit Contact"

        if (isUpdate && contact != null) {
            newContact.setText(contact.name)
            contactEmail.setText(contact.email)
        }

        alertDialogBuilderUserInput
            .setCancelable(false)
            .setPositiveButton(
                if (isUpdate) "Update" else "Save"
            ) { dialogBox, id -> }
            .setNegativeButton(
                "Delete"
            ) { dialogBox, id ->
                if (isUpdate) {
                    deleteContact(contact!!, position)
                } else {

                    dialogBox.cancel()

                }
            }


        val alertDialog = alertDialogBuilderUserInput.create()
        alertDialog.show()

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(View.OnClickListener {
            if (TextUtils.isEmpty(newContact.text.toString())) {
                Toast.makeText(this@MainActivity, "Enter contact name!", Toast.LENGTH_SHORT).show()
                return@OnClickListener
            } else {
                alertDialog.dismiss()
            }


            if (isUpdate && contact != null) {

                updateContact(newContact.text.toString(), contactEmail.text.toString(), position)
            } else {

                createContact(newContact.text.toString(), contactEmail.text.toString())
            }
        })
    }

    private fun deleteContact(contact: Contact, position: Int) {

        contactArrayList.removeAt(position)
//        db.deleteContact(contact)
        contactAppDatabase.getContactDAO().deleteContact(contact)
        contactsAdapter!!.notifyDataSetChanged()
    }

    private fun updateContact(name: String, email: String, position: Int) {

        val contact = contactArrayList.get(position)

        contact.name = name
        contact.email = email

//        db.updateContact(contact)
        contactAppDatabase.getContactDAO().updateContact(contact)

        contactArrayList[position] = contact

        contactsAdapter?.notifyDataSetChanged()


    }

    private fun createContact(name: String, email: String) {

//        val id = db.insertContact(name, email)
        val id = contactAppDatabase.getContactDAO().addContact(Contact(name,email,0))

        //        val contact = db.getContact(id)
        val contact = contactAppDatabase.getContactDAO().getContact(id)

        contactArrayList.add(0, contact)
        contactsAdapter!!.notifyDataSetChanged()


    }
}
