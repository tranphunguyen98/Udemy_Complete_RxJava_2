package com.tranphunguyen.tmdbmovie.view

import android.app.SearchManager
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.tranphunguyen.tmdbmovie.R

class SearchableActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_searchable)

        Log.d("TestSearch","Toi luon!")

        supportActionBar?.setDisplayHomeAsUpEnabled(false)
        supportActionBar?.setIcon(null)

        // Verify the action and get the query
        if (Intent.ACTION_SEARCH == intent.action) {
            intent.getStringArrayExtra(SearchManager.QUERY)?.also { query ->
                Log.d("TestSearch",query.toString())
            }
        }
    }
}
