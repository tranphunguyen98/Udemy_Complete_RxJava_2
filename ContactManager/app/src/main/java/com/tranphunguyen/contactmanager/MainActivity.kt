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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tranphunguyen.contactmanager.ViewModel.ContactViewmodel

import com.tranphunguyen.contactmanager.adapter.ContactsAdapter
import com.tranphunguyen.contactmanager.db.entity.Contact
import com.tranphunguyen.contactmanager.db.ContactsAppDatabase
import io.reactivex.disposables.CompositeDisposable
import kotlinx.android.synthetic.main.activity_main.*

import java.util.ArrayList


class MainActivity : AppCompatActivity() {


    private val contactArrayList = ArrayList<Contact>()
    private lateinit var recyclerView: RecyclerView

    private lateinit var contactAppDatabase: ContactsAppDatabase

    private val composite = CompositeDisposable()

    private val contactsAdapter by lazy {

        ContactsAdapter(contactArrayList)

    }

    private val contactViewmodel by lazy {

        ViewModelProviders.of(this).get(ContactViewmodel::class.java)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)
        supportActionBar?.title = "Contacts Manager"

        recyclerView = findViewById(R.id.recycler_view_contacts)

        val mLayoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = mLayoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = contactsAdapter

        fab.setOnClickListener { addAndEditContacts(false, null, -1) }

        contactViewmodel.getAllContacts().observe(this,
            Observer<List<Contact>> { contacts ->
                contactArrayList.clear()
                contactArrayList.addAll(contacts)
                contactsAdapter.notifyDataSetChanged()
            })
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
                    deleteContact(contact!!)
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

    private fun deleteContact(contact: Contact) {

        contactViewmodel.deleteContact(contact)

    }

    private fun updateContact(name: String, email: String, position: Int) {

        val contact = contactArrayList.get(position)

        contact.name = name
        contact.email = email

        contactViewmodel.updateContact(contact)


    }

    private fun createContact(name: String, email: String) {

        contactViewmodel.createContact(name,email)

    }

    override fun onDestroy() {
        super.onDestroy()

        composite.dispose()
    }
}
