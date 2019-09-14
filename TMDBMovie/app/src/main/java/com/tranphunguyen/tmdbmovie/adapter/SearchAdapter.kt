package com.tranphunguyen.tmdbmovie.adapter

import androidx.recyclerview.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.search_list_item.view.*

/**
 * Created by Trần Phú Nguyện on 8/14/2019.
 */

class SearchAdapter(var listSuggestion: ArrayList<Movie>): androidx.recyclerview.widget.RecyclerView.Adapter<SearchAdapter.SearchHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchHolder {

        val view = LayoutInflater.from(parent.context).inflate(R.layout.search_list_item,parent,false)

        return SearchHolder(view)

    }

    override fun getItemCount(): Int = listSuggestion.size

    override fun onBindViewHolder(holder: SearchHolder, p1: Int) {

        holder.tvSearch.text = listSuggestion[p1].title

    }


    inner class SearchHolder(view: View): androidx.recyclerview.widget.RecyclerView.ViewHolder(view),View.OnClickListener {
        override fun onClick(v: View?) {

           Log.d("TestSEarch1",listSuggestion[position].title)

        }

        val tvSearch = view.search_item
    }
}