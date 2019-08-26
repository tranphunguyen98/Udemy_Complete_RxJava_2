package com.tranphunguyen.contactmanager.ViewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tranphunguyen.contactmanager.Repository.ContactRepository
import com.tranphunguyen.contactmanager.db.entity.Contact


/**
 * Created by Trần Phú Nguyện on 8/26/2019.
 */

class ContactViewmodel(application: Application) : AndroidViewModel(application) {

    private val contactRepository by lazy {
        ContactRepository(application)
    }

    fun getAllContacts(): LiveData<List<Contact>> {

        return contactRepository.contactsLiveData

    }


    fun createContact(name: String, email: String) {

        contactRepository.createContact(Contact(name,email,0))
    }

    fun updateContact(contact: Contact) {

        contactRepository.updateContact(contact)
    }

    fun deleteContact(contact: Contact) {

        contactRepository.deleteContact(contact)
    }

    fun clear() {

        contactRepository.clear()
    }

}