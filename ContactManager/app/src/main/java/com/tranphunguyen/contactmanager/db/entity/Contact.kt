package com.tranphunguyen.contactmanager.db.entity

data class Contact(
    var name: String,
    var email: String,
    var id: Int
) {
    companion object {

        const val TABLE_NAME = "contacts"

        const val COLUMN_ID = "contact_id"
        const val COLUMN_NAME = "contact_name"
        const val COLUMN_EMAIL = "contact_email"

        // Create table SQL query
        const val CREATE_TABLE = (
                "CREATE TABLE " + TABLE_NAME + "("
                        + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                        + COLUMN_NAME + " TEXT,"
                        + COLUMN_EMAIL + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                        + ")")
    }
}
