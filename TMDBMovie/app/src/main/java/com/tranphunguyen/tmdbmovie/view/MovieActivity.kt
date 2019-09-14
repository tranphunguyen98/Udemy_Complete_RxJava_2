package com.tranphunguyen.tmdbmovie.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.transition.DrawableCrossFadeFactory
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.activity_movie.*
import kotlinx.android.synthetic.main.content_movie.*

class MovieActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)

        setSupportActionBar(toolbar)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val currentMovie = intent?.getParcelableExtra<Movie>("movie")

        Log.d("TMDBMoviewne", currentMovie?.title)


        currentMovie?.let {

            supportActionBar?.title = it.title

            val pathImage = "https://image.tmdb.org/t/p/w500${currentMovie.poster_path}"

            val factory = DrawableCrossFadeFactory.Builder().setCrossFadeEnabled(true).build()

            Glide
                .with(this)
                .load(pathImage)
                .transition(DrawableTransitionOptions.withCrossFade(factory))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.color.placeholder_bg)
                .into(ivMovieLarge)

            tvMovieTitle.text = currentMovie.title
            tvMovieRating.text = currentMovie.vote_average.toString()
            tvReleaseDate.text = currentMovie.release_date
            tvPlotsynopsis.text = "${currentMovie.overview} \n\n ${currentMovie.overview}"
        }

    }
}
