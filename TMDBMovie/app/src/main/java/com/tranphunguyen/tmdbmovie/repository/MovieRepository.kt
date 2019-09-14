package com.tranphunguyen.tmdbmovie.repository

import android.app.Application
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.Movie
import com.tranphunguyen.tmdbmovie.service.MovieService
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import kotlin.collections.ArrayList

/**
 * Created by Trần Phú Nguyện on 9/12/2019.
 */
class MovieRepository(private val application: Application) {

    val movieLiveData = MutableLiveData<List<Movie>>()

    private val service = MovieService.instance

    private val compositeDisposable = CompositeDisposable()

    val listMovie = ArrayList<Movie>()

    private var page = 0

    fun getMovies() {



        compositeDisposable.add(

            service.getPopularMoviesWithRx(1,application.getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { movieRes ->Observable.fromIterable(movieRes.results)  }
                .filter { movie -> movie.vote_average > 1 }
                .subscribeWith(object :DisposableObserver<Movie>() {
                    override fun onComplete() {
                        movieLiveData.postValue(listMovie)
                    }

                    override fun onNext(movie: Movie) {

                        listMovie.add(movie)

                    }

                    override fun onError(e: Throwable) {

                    }

                })

        )

    }

    fun getMore() {

        Log.d("TestMVVM Repo",listMovie.size.toString())

        page++

        compositeDisposable.add(

            service.getPopularMoviesWithRx(page,application.getString(R.string.api_key))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap { movieRes -> Observable.fromIterable(movieRes.results)  }
                .filter { movie -> movie.vote_average > 1 }
                .subscribeWith(object :DisposableObserver<Movie>() {
                    override fun onComplete() {

                        movieLiveData.postValue(listMovie)

                    }

                    override fun onNext(movie: Movie) {

                        listMovie.add(movie)

                    }

                    override fun onError(e: Throwable) {

                    }

                })

        )

    }

    fun clear() {
        compositeDisposable.clear()
    }

}