package com.tranphunguyen.tmdbmovie.adapter

import android.content.Intent
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.tranphunguyen.tmdbmovie.view.MovieActivity
import kotlinx.android.synthetic.main.item_progressbar.view.*


/**
 * Created by Trần Phú Nguyện on 7/25/2019.
 */
class MovieAdapter(var listMovie: ArrayList<Movie?>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    // for load more
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var onLoadMoreListener: OnLoadMoreListener? = null

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var isLoading: Boolean = false
    private val visibleThreshold = 5
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    interface OnLoadMoreListener {
        fun onLoadMore()
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        // load more
        val linearLayoutManager = recyclerView.layoutManager as GridLayoutManager
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                Log.d("checkRecyclerViewNot", "$totalItemCount $lastVisibleItem $visibleThreshold ")

                if(!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold) ) {

                    Log.d("checkRecyclerView", "$totalItemCount $lastVisibleItem $visibleThreshold ")

                    onLoadMoreListener?.onLoadMore()
                    isLoading = true

                }

            }
        })
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

        when(viewType) {

            VIEW_TYPE_LOADING -> {

                val view = LayoutInflater.from(p0.context).inflate(com.tranphunguyen.tmdbmovie.R.layout.item_progressbar,p0,false)
                return ViewHolderLoading(view)

            }

            else -> {

                val view = LayoutInflater.from(p0.context).inflate(com.tranphunguyen.tmdbmovie.R.layout.movie_list_item,p0,false)
                return MoviewHolder(view)

            }



        }


    }

    fun setLoaded() {
        isLoading = false

    }

    fun remove(position: Int) {
        listMovie.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun getItemViewType(position: Int): Int {
        return if (listMovie[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is MoviewHolder -> {
                holder.tvTitle.text = listMovie[position]?.title
                holder.tvRating.text = listMovie[position]?.vote_average.toString()

                val pathImage = "https://image.tmdb.org/t/p/w500${listMovie[position]?.poster_path}"

                val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

                Glide
                    .with(holder.itemView.context)
                    .load(pathImage)
                    .transition(withCrossFade(factory))
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .placeholder(com.tranphunguyen.tmdbmovie.R.color.placeholder_bg)
                    .into(holder.ivMovie)
            }

            is ViewHolderLoading -> {

                holder.progressBar.isIndeterminate = true

            }
        }


    }

    fun setOnLoadMoreListener(mOnLoadMoreListener: OnLoadMoreListener) {

        this.onLoadMoreListener = mOnLoadMoreListener

    }

    private inner class ViewHolderLoading(view: View) : RecyclerView.ViewHolder(view) {

        val progressBar = view.progressbar

    }

    private inner class MoviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMovie = view.iv_movie
        val tvTitle = view.tv_title
        val tvRating = view.tv_rating

        init {
            view.setOnClickListener {

                val position = this.adapterPosition

                if(position != RecyclerView.NO_POSITION) {

                    val intent = Intent(view.context,MovieActivity::class.java)

                    val currentMovie = listMovie[position]

                    intent.putExtra("movie", currentMovie)

                    view.context.startActivity(intent)

                }

            }
        }
    }
}