/*
 * Copyright (c) 2019 Razeware LLC
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.tranphunguyen.tmdbmovie.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import com.tranphunguyen.tmdbmovie.R
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.*

class MainActivity : AppCompatActivity(), ImageRequester.ImageRequesterResponse {

  private var photosList: ArrayList<Photo> = ArrayList()
  private lateinit var imageRequester: ImageRequester
  private lateinit var linearLayoutManager: androidx.recyclerview.widget.LinearLayoutManager
  private lateinit var adapter: RecyclerAdapter
  private lateinit var gridLayoutManager: androidx.recyclerview.widget.GridLayoutManager

  private val lastVisibleItemPosition: Int
    get() = if (recyclerView.layoutManager == linearLayoutManager) {
      linearLayoutManager.findLastVisibleItemPosition()
    } else {
      gridLayoutManager.findLastVisibleItemPosition()
    }


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)
    linearLayoutManager = androidx.recyclerview.widget.LinearLayoutManager(
        this,
        androidx.recyclerview.widget.LinearLayoutManager.VERTICAL,
        false
    )
    recyclerView.layoutManager = linearLayoutManager
    adapter = RecyclerAdapter(photosList)
    recyclerView.adapter = adapter
    setRecyclerViewScrollListener()
    gridLayoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)
    setRecyclerViewItemTouchListener()


    imageRequester = ImageRequester(this)
  }

  private fun changeLayoutManager() {
    if (recyclerView.layoutManager == linearLayoutManager) {
      //1
      recyclerView.layoutManager = gridLayoutManager
      //2
      if (photosList.size == 1) {
        requestPhoto()
      }
    } else {
      //3
      recyclerView.layoutManager = linearLayoutManager
    }
  }

  private fun setRecyclerViewScrollListener() {
    recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
      override fun onScrollStateChanged(recyclerView: androidx.recyclerview.widget.RecyclerView, newState: Int) {
        super.onScrollStateChanged(recyclerView, newState)
        val totalItemCount = recyclerView.layoutManager!!.itemCount
        if (!imageRequester.isLoadingData && totalItemCount == lastVisibleItemPosition + 1) {
          requestPhoto()
        }
      }
    })
  }

  private fun setRecyclerViewItemTouchListener() {

    //1
    val itemTouchCallback = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT) {
      override fun onMove(recyclerView: androidx.recyclerview.widget.RecyclerView, viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, viewHolder1: androidx.recyclerview.widget.RecyclerView.ViewHolder): Boolean {
        //2
        return false
      }

      override fun onSwiped(viewHolder: androidx.recyclerview.widget.RecyclerView.ViewHolder, swipeDir: Int) {
        //3
        val position = viewHolder.adapterPosition
        photosList.removeAt(position)
        recyclerView.adapter!!.notifyItemRemoved(position)
      }
    }

    //4
    val itemTouchHelper = ItemTouchHelper(itemTouchCallback)
    itemTouchHelper.attachToRecyclerView(recyclerView)
  }

  override fun onCreateOptionsMenu(menu: Menu): Boolean {
    menuInflater.inflate(R.menu.menu_main, menu)
    return true
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    if (item.itemId == R.id.action_change_recycler_manager) {
      changeLayoutManager()
      return true
    }
    return super.onOptionsItemSelected(item)
  }

  override fun onStart() {
    super.onStart()
    if (photosList.size == 0) {
      requestPhoto()
    }

  }

  private fun requestPhoto() {
    try {
      imageRequester.getPhoto()
    } catch (e: IOException) {
      e.printStackTrace()
    }

  }

  override fun receivedNewPhoto(newPhoto: Photo) {
    runOnUiThread {
      photosList.add(newPhoto)
      adapter.notifyItemInserted(photosList.size-1)
    }
  }
}
