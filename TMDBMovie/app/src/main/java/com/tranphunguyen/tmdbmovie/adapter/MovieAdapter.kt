package com.tranphunguyen.tmdbmovie.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.bumptech.glide.Glide
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.movie_list_item.view.*

/**
 * Created by Trần Phú Nguyện on 7/25/2019.
 */
class MovieAdapter(var listMovie: ArrayList<Movie>) : RecyclerView.Adapter<MovieAdapter.MoviewHolder>() {

    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): MoviewHolder {

        val view = LayoutInflater.from(p0.context).inflate(R.layout.movie_list_item,p0,false)

        return MoviewHolder(view)

    }

    override fun getItemCount(): Int = listMovie.size

    override fun onBindViewHolder(holder: MoviewHolder, position: Int) {

        holder.tvTitle.text = listMovie[position].title
        holder.tvRating.text = listMovie[position].vote_average.toString()

        val pathImage = "https://image.tmdb.org/t/p/w500${listMovie[position].poster_path}"

        Glide
            .with(holder.itemView.context)
            .load(pathImage)
            .into(holder.ivMovie)

    }

    class MoviewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val ivMovie = view.iv_movie
        val tvTitle = view.tv_title
        val tvRating = view.tv_rating
    }
}