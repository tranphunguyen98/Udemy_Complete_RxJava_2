package com.tranphunguyen.tmdbmovie.view

import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.tranphunguyen.tmdbmovie.service.MovieService
import android.util.Log
import android.view.Menu
import android.view.MenuItem
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
import com.miguelcatalan.materialsearchview.MaterialSearchView
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.viewModel.ViewModelMain

class MainActivity : AppCompatActivity() {

    private val movieAdapter = MovieAdapter(ArrayList())

    private val viewModelMain by lazy {

        ViewModelProviders.of(this).get(ViewModelMain::class.java)

    }

    override
    fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbar)

        getPopularMovieWithRx()

        refresh.setColorSchemeResources(R.color.colorPrimary)
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

                getMore()
            }
        })

        searchViewCode()

    }


    private fun searchViewCode() {
        search_view.setSuggestions(arrayOf("ABC", "A", "B", "aaa"))
        search_view.showSuggestions()
        search_view.setEllipsize(true)
        search_view.setOnQueryTextListener(object : MaterialSearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                Toast.makeText(applicationContext, query, Toast.LENGTH_SHORT).show()
                return false
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        search_view.setOnSearchViewListener(object : MaterialSearchView.SearchViewListener {
            override fun onSearchViewShown() {}

            override fun onSearchViewClosed() {}
        })
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)

        val item = menu?.findItem(R.id.action_search)
        search_view.setMenuItem(item)

        return true
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return when (item?.itemId) {

            R.id.action_search -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

    /*  private fun getPopularMovie() {

         page++

         val service = MovieService.instance

         call = service.getPopularMovies(page, this.getString(R.string.api_key))

         call?.enqueue(object : Callback<MovieDBResponse> {
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
     */


    private fun getPopularMovieWithRx() {

        viewModelMain.getMovies().observe(this,

            Observer { movies ->

                movieAdapter.listMovie = movies as ArrayList<Movie>

                Log.d("TestMVVM Main1",movieAdapter.listMovie.size.toString())
                Log.d("TestMVVM Main",movies.size.toString())

                progressbar.visibility = View.GONE

//                movieAdapter.notifyItemInserted(movieAdapter.listMovie.size - 1)

                movieAdapter.notifyDataSetChanged()
                movieAdapter.setLoaded()
                movieAdapter.setLoadedProgressBar()



            }

        )

        showOnRecyclerView()

    }

    fun getMore() {

        viewModelMain.getMore()

    }

    private fun showOnRecyclerView() {

        if (this.resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 2)

        } else {

            recyclerView.layoutManager = androidx.recyclerview.widget.GridLayoutManager(this, 4)

        }

        recyclerView.itemAnimator = androidx.recyclerview.widget.DefaultItemAnimator()
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

    override fun onBackPressed() {
        if (search_view.isSearchOpen) {
            search_view.closeSearch()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        viewModelMain.clear()

//        call?.let {
//            if(it.isExecuted)
//                it.cancel()
//        }

    }
}
