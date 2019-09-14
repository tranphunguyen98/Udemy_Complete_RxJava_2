package com.tranphunguyen.contactmanager.Repository

import android.app.Application
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.tranphunguyen.contactmanager.db.ContactsAppDatabase
import com.tranphunguyen.contactmanager.db.entity.Contact
import io.reactivex.Completable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableCompletableObserver
import io.reactivex.schedulers.Schedulers

/**
 * Created by Trần Phú Nguyện on 8/26/2019.
 */

class ContactRepository(private val application: Application) {

    private val disposables = CompositeDisposable()
    val contactsLiveData = MutableLiveData<List<Contact>>()

    private val contactAppDatabase by lazy {

        Room.databaseBuilder(
            application,
            ContactsAppDatabase::class.java,
            "ContactDB"
        ).build()

    }

    init {

        disposables.add(
            contactAppDatabase.getContactDAO().getContacts()
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                    { contacts ->

                        Log.d("TestRx", contacts.size.toString() + contacts[0].name)

                        contactsLiveData.postValue(contacts)

                    },
                    {

                    }
                )
        )

    }

    fun createContact(contact: Contact) {

        disposables.add(
            Completable.fromAction {

                contactAppDatabase.getContactDAO().addContact(contact)

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(

                    object: DisposableCompletableObserver() {
                        override fun onComplete() {

                            Toast.makeText(application.applicationContext, "Create successful!", Toast.LENGTH_SHORT).show()

                        }

                        override fun onError(e: Throwable) {

                            Toast.makeText(application.applicationContext, "Create successful!", Toast.LENGTH_SHORT).show()

                        }

                    }

                )
        )

    }

    fun updateContact(contact: Contact) {

        disposables.add(
            Completable.fromAction {

                contactAppDatabase.getContactDAO().updateContact(contact)

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(

                    object: DisposableCompletableObserver() {
                        override fun onComplete() {

                            Toast.makeText(application.applicationContext, "Update successful!", Toast.LENGTH_SHORT).show()

                        }

                        override fun onError(e: Throwable) {

                            Toast.makeText(application.applicationContext, "Update successful!", Toast.LENGTH_SHORT).show()

                        }

                    }

                )
        )


    }

    fun deleteContact(contact: Contact) {

        disposables.add(
            Completable.fromAction {

                contactAppDatabase.getContactDAO().deleteContact(contact)

            }.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(

                    object: DisposableCompletableObserver() {
                        override fun onComplete() {

                            Toast.makeText(application.applicationContext, "Delete successful!", Toast.LENGTH_SHORT).show()


                        }

                        override fun onError(e: Throwable) {

                            Toast.makeText(application.applicationContext, "Delete successful!", Toast.LENGTH_SHORT).show()

                        }

                    }

                )
        )

    }

    fun clear() {
        disposables.clear()
    }
}