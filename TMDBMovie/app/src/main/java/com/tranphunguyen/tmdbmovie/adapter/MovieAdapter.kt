package com.tranphunguyen.tmdbmovie.adapter

import android.content.Intent
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
class MovieAdapter(var listMovie: ArrayList<Movie>) : androidx.recyclerview.widget.RecyclerView.Adapter<androidx.recyclerview.widget.RecyclerView.ViewHolder>() {

    // for load more
    private val VIEW_TYPE_ITEM = 0
    private val VIEW_TYPE_LOADING = 1
    private var onLoadMoreListener: OnLoadMoreListener? = null
    private var onLoadingProgressBar: OnLoadingProgressBar? = null

    // The minimum amount of items to have below your current scroll position
    // before loading more.
    private var isLoading: Boolean = false
    private var isLoadingProgressBar: Boolean = false
    private val visibleThreshold = 5
    private var lastVisibleItem: Int = 0
    private var totalItemCount: Int = 0

    interface OnLoadMoreListener {
        fun onLoadMore()
    }
    interface OnLoadingProgressBar {
        fun onLoading()
    }

    override fun onAttachedToRecyclerView(recyclerView: androidx.recyclerview.widget.RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)

        // load more
        val linearLayoutManager = recyclerView.layoutManager as androidx.recyclerview.widget.GridLayoutManager

        recyclerView.addOnScrollListener(object : androidx.recyclerview.widget.RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: androidx.recyclerview.widget.RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                totalItemCount = linearLayoutManager.itemCount
                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                if (!isLoading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                    onLoadMoreListener?.onLoadMore()

                    isLoading = true

                }

                if(!isLoadingProgressBar && lastVisibleItem + 1 >= totalItemCount  ) {

                    onLoadingProgressBar?.onLoading()

                    isLoadingProgressBar = true

                }

            }
        })
    }

    override fun onCreateViewHolder(p0: ViewGroup, viewType: Int): androidx.recyclerview.widget.RecyclerView.ViewHolder {

        when (viewType) {

            VIEW_TYPE_LOADING -> {

                val view = LayoutInflater.from(p0.context)
                    .inflate(com.tranphunguyen.tmdbmovie.R.layout.item_progressbar, p0, false)
                return ViewHolderLoading(view)

            }

            else -> {

                val view = LayoutInflater.from(p0.context)
                    .inflate(com.tranphunguyen.tmdbmovie.R.layout.movie_list_item, p0, false)
                return MoviewHolder(view)

            }


        }

    }

    fun setLoaded() {
        isLoading = false

    }

    fun setLoadedProgressBar() {
        isLoadingProgressBar = false
    }

    fun remove(position: Int) {
        listMovie.removeAt(position)
        notifyItemRemoved(position)
    }

    override fun getItemCount(): Int = listMovie.size

    override fun getItemViewType(position: Int): Int {
//        return if (listMovie[position] == null) VIEW_TYPE_LOADING else VIEW_TYPE_ITEM
        return VIEW_TYPE_ITEM
    }

    override fun onBindViewHolder(holder: androidx.recyclerview.widget.RecyclerView.ViewHolder, position: Int) {

        when (holder) {

            is MoviewHolder -> {
                holder.tvTitle.text = listMovie[position].title
                holder.tvRating.text = listMovie[position].vote_average.toString()

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

    fun setOnLoadingProgressBar(mOnLoadingProgressBar: OnLoadingProgressBar) {

        this.onLoadingProgressBar = mOnLoadingProgressBar

    }

    private inner class ViewHolderLoading(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

        val progressBar = view.progressbar

    }

    private inner class MoviewHolder(view: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {
        val ivMovie = view.iv_movie
        val tvTitle = view.tv_title
        val tvRating = view.tv_rating

        init {
            view.setOnClickListener {

                val position = this.adapterPosition

                if (position != androidx.recyclerview.widget.RecyclerView.NO_POSITION) {

                    val intent = Intent(view.context, MovieActivity::class.java)

                    val currentMovie = listMovie[position]

                    intent.putExtra("movie", currentMovie)

                    view.context.startActivity(intent)

                }

            }
        }
    }
}