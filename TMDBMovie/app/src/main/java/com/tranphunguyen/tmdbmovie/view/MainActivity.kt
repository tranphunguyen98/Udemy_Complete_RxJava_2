package com.tranphunguyen.tmdbmovie.view

import android.content.res.Configuration
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tranphunguyen.tmdbmovie.service.MovieService
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.GridLayoutManager
import android.util.Log
import android.view.View
import com.tranphunguyen.tmdbmovie.adapter.MovieAdapter
import com.tranphunguyen.tmdbmovie.adapter.MovieAdapter.*
import com.tranphunguyen.tmdbmovie.model.Movie
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import com.tranphunguyen.tmdbmovie.model.MovieDBResponse


class MainActivity : AppCompatActivity() {

    private val movieAdapter = MovieAdapter(ArrayList())

    val service = MovieService.instance

    //    private var call: Call<MovieDBResponse>? = null
    private lateinit var movieResObservable: Observable<MovieDBResponse>

    private val compositeDisposable = CompositeDisposable()

    private var page = 1

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.tranphunguyen.tmdbmovie.R.layout.activity_main)

        getPopularMovieWithRx()

        refresh.setColorSchemeResources(com.tranphunguyen.tmdbmovie.R.color.colorPrimary)
        refresh.setOnRefreshListener {

            getPopularMovieWithRx()

        }

        movieAdapter.setOnLoadingProgressBar(object : OnLoadingProgressBar {
            override fun onLoading() {

                progressbar.visibility = View.VISIBLE
            }
        })

        movieAdapter.setOnLoadMoreListener(object : OnLoadMoreListener {
            override fun onLoadMore() {


                addPopularMovie()
            }
        })


    }

//    private fun getPopularMovie() {
//
//        page++
//
//        val service = MovieService.instance
//
//        call = service.getPopularMovies(page, this.getString(R.string.api_key))
//
//        call?.enqueue(object : Callback<MovieDBResponse> {
//            override fun onFailure(call: Call<MovieDBResponse>, t: Throwable) {
//
//            }
//
//            override fun onResponse(call: Call<MovieDBResponse>, response: Response<MovieDBResponse>) {
//
//                val movieDBResponse = response.body()
//
//                movieDBResponse?.results?.let {
//                    showOnRecyclerView(it)
//                }
//
//            }
//
//        })
//
//    }


    private fun getPopularMovieWithRx() {

        compositeDisposable.add(
            service
                .getPopularMoviesWithRx(1, this.getString(com.tranphunguyen.tmdbmovie.R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { movieRes -> Observable.fromIterable(movieRes.results) }
                .filter { movie -> movie.vote_average > 7.0 }
                .subscribeWith(object : DisposableObserver<Movie>() {
                    override fun onComplete() {
                        showOnRecyclerView()
                    }

                    override fun onNext(movie: Movie) {
                        movieAdapter.listMovie.add(movie)
                    }

                    override fun onError(e: Throwable) {
                    }

                })
        )

        Log.d("checkComposite", compositeDisposable.size().toString())
    }

    fun addPopularMovie() {

        compositeDisposable.add(
            service
                .getPopularMoviesWithRx(page, this.getString(com.tranphunguyen.tmdbmovie.R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { movieRes -> Observable.fromIterable(movieRes.results) }
                .filter { movie -> movie.vote_average > 7.0 }
                .subscribeWith(object : DisposableObserver<Movie>() {
                    override fun onComplete() {

                        progressbar.visibility = View.GONE

                        movieAdapter.notifyItemInserted(movieAdapter.listMovie.size - 1)
                        movieAdapter.setLoaded()
                        movieAdapter.setLoadedProgressBar()
                    }

                    override fun onNext(movie: Movie) {
                        movieAdapter.listMovie.add(movie)
                    }

                    override fun onError(e: Throwable) {
                    }

                })
        )

        page++

        Log.d("checkComposite", compositeDisposable.size().toString())


    }

    private fun showOnRecyclerView() {

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

    override fun onDestroy() {
        super.onDestroy()

        compositeDisposable.clear()

//        call?.let {
//            if(it.isExecuted)
//                it.cancel()
//        }

    }
}
