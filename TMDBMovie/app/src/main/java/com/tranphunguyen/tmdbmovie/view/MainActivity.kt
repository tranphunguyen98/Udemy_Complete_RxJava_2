package com.tranphunguyen.tmdbmovie.view

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.MovieDBResponse
import com.tranphunguyen.tmdbmovie.service.MovieService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import com.tranphunguyen.tmdbmovie.adapter.MovieAdapter
import com.tranphunguyen.tmdbmovie.model.Movie
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private lateinit var movieAdapter: MovieAdapter
    private var page = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        getPopularMovie()

        refresh.setColorSchemeResources(R.color.colorPrimary)
        refresh.setOnRefreshListener {

            getPopularMovie()

        }

    }

    fun getPopularMovie() {

        page++

        val service = MovieService.instance

        val call = service.getPopularMovies(page,this.getString(R.string.api_key))

        call.enqueue(object : Callback<MovieDBResponse> {
            override fun onFailure(call: Call<MovieDBResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieDBResponse>, response: Response<MovieDBResponse>) {

                val movieDBResponse = response.body()

                movieDBResponse?.results?.let {
                    showOnRecyclerView(it)
                }

            }

        })

    }

    private fun showOnRecyclerView(movies: ArrayList<Movie>) {



        movieAdapter = MovieAdapter(movies)

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.layoutManager = GridLayoutManager(this, 2)

        } else {

            recyclerView.layoutManager = GridLayoutManager(this, 4)

        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = movieAdapter
        movieAdapter.notifyDataSetChanged()

        refresh.isRefreshing = false

    }
}
