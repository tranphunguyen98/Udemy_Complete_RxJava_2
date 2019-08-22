package com.tranphunguyen.contactmanager.db

import androidx.room.*
import com.tranphunguyen.contactmanager.db.entity.Contact

/**
 * Created by Trần Phú Nguyện on 8/22/2019.
 */

@Dao
public interface ContactDAO {

    @Insert
    fun addContact(contact: Contact): Long

    @Update
    fun updateContact(contact: Contact)

    @Delete
    fun deleteContact(contact: Contact)

    @Query("select * from contacts")
    fun getContacts(): List<Contact>

    @Query("select * from contacts where contact_id ==:contactId")
    fun getContact(contactId: Long): Contact

}