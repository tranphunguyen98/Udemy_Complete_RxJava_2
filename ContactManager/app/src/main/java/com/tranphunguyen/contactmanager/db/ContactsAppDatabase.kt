package com.tranphunguyen.contactmanager.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.tranphunguyen.contactmanager.db.entity.Contact

/**
 * Created by Trần Phú Nguyện on 8/22/2019.
 */

@Database(entities = [Contact::class],version = 1)
abstract class ContactsAppDatabase: RoomDatabase() {

    abstract fun getContactDAO(): ContactDAO

}