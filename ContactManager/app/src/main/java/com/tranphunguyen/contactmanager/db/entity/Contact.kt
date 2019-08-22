package com.tranphunguyen.contactmanager.db.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contacts")
data class Contact(

    @ColumnInfo(name = "contact_name")
    var name: String,

    @ColumnInfo(name = "contact_email")
    var email: String,

    @ColumnInfo(name = "contact_id")
    @PrimaryKey(autoGenerate = true)
    var id: Long
)
//{
//    companion object {
//
//        const val TABLE_NAME = "contacts"
//
//        const val COLUMN_ID = "contact_id"
//        const val COLUMN_NAME = "contact_name"
//        const val COLUMN_EMAIL = "contact_email"
//
//        // Create table SQL query
//        const val CREATE_TABLE = (
//                "CREATE TABLE " + TABLE_NAME + "("
//                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
//                        + COLUMN_NAME + " TEXT,"
//                        + COLUMN_EMAIL + " DATETIME DEFAULT CURRENT_TIMESTAMP"
//                        + ")")
//    }
//}
