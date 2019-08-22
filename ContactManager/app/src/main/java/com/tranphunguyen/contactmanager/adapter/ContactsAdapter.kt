package com.tranphunguyen.contactmanager.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView


import com.tranphunguyen.contactmanager.MainActivity
import com.tranphunguyen.contactmanager.R
import com.tranphunguyen.contactmanager.db.entity.Contact

import java.util.ArrayList


class ContactsAdapter(
    private var contactssList: ArrayList<Contact>
) : RecyclerView.Adapter<ContactsAdapter.MyViewHolder>() {


    override fun getItemCount(): Int = contactssList.size

    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val name: TextView = view.findViewById(R.id.name)
        val emil: TextView = view.findViewById(R.id.email)

    }

    override
    fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.contact_list_item, parent, false)

        return MyViewHolder(itemView)

    }

    override
    fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val contact = contactssList[position]

        holder.name.text = contact.name
        holder.emil.text = contact.email

        holder.itemView.setOnClickListener {

            val mainActivity = holder.itemView.context as MainActivity

            if (holder.itemView.context is MainActivity) {
                Log.d("CheckMainactivity", "True")
            } else {
                Log.d("CheckMainactivity", "false")

            }

            mainActivity.addAndEditContacts(true, contact, position)
        }

    }

}



