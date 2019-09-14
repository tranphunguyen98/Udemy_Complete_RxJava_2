package com.tranphunguyen.tmdbmovie.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tranphunguyen.tmdbmovie.model.Movie
import com.tranphunguyen.tmdbmovie.repository.MovieRepository

/**
 * Created by Trần Phú Nguyện on 9/14/2019.
 */
class ViewModelMain(application: Application): AndroidViewModel(application) {

    private val movieRepository: MovieRepository by lazy {

        MovieRepository(application)

    }

    fun getMovies(): MutableLiveData<List<Movie>> {

        movieRepository.getMovies()

        return movieRepository.movieLiveData
    }

    fun getMore() {

        movieRepository.getMore()

    }

    fun clear() {

        movieRepository.clear()

    }
}