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
import android.view.View
import com.tranphunguyen.tmdbmovie.adapter.MovieAdapter
import com.tranphunguyen.tmdbmovie.adapter.MovieAdapter.*
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

        movieAdapter = MovieAdapter(ArrayList())

        movieAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {

                movieAdapter.listMovie.add(null)
                movieAdapter.listMovie.add(null)

                movieAdapter.notifyItemInserted(movieAdapter.listMovie.size - 1)
                addPopularMovie()

            }
        })

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

    fun addPopularMovie() {

        page++

        val service = MovieService.instance

        val call = service.getPopularMovies(page,this.getString(R.string.api_key))

        call.enqueue(object : Callback<MovieDBResponse> {
            override fun onFailure(call: Call<MovieDBResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieDBResponse>, response: Response<MovieDBResponse>) {

                val movieDBResponse = response.body()

                movieDBResponse?.results?.let {

                    movieAdapter.remove(movieAdapter.listMovie.size - 1)
                    movieAdapter.notifyItemRemoved(movieAdapter.listMovie.size)

                    movieAdapter.remove(movieAdapter.listMovie.size - 1)
                    movieAdapter.notifyItemRemoved(movieAdapter.listMovie.size)

                    movieAdapter.listMovie.addAll(it)
                    movieAdapter.notifyDataSetChanged()
                    movieAdapter.setLoaded()
                }

            }

        })

    }

    private fun showOnRecyclerView(movies: ArrayList<Movie>) {

        movieAdapter.listMovie = movies as ArrayList<Movie?>

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.layoutManager = GridLayoutManager(this, 2)

        } else {

            recyclerView.layoutManager = GridLayoutManager(this, 4)

        }

        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = movieAdapter
        movieAdapter.notifyDataSetChanged()

        shimmer_view_container.stopShimmer()
        shimmer_view_container.visibility = View.GONE
        refresh.isRefreshing = false

    }

    override fun onResume() {
        super.onResume()
        shimmer_view_container.startShimmer()
    }

//    override fun onPause() {
//        super.onPause()
//        shimmer_view_container.stopShimmer()
//    }
}
