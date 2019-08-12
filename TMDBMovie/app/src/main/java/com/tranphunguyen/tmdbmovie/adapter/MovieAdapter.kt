package com.tranphunguyen.tmdbmovie.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.movie_list_item.view.*
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade


/**
 * Created by Trần Phú Nguyện on 7/25/2019.
 */
class MovieAdapter(var listMovie: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MoviewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MoviewHolder {

        val view = LayoutInflater.from(p0.context).inflate(com.tranphunguyen.tmdbmovie.R.layout.movie_list_item,p0,false)

        return MoviewHolder(view)

    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: MoviewHolder, position: Int) {

        holder.tvTitle.text = listMovie[position].title
        holder.tvRating.text = listMovie[position].vote_average.toString()

        val pathImage = "https://image.tmdb.org/t/p/w500${listMovie[position].poster_path}"


        val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

        Glide
            .with(holder.itemView.context)
            .load(pathImage)
            .transition(withCrossFade(factory))
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .placeholder(com.tranphunguyen.tmdbmovie.R.color.placeholder_bg)
            .into(holder.ivMovie)

    }

    class MoviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMovie = view.iv_movie
        val tvTitle = view.tv_title
        val tvRating = view.tv_rating
    }
}