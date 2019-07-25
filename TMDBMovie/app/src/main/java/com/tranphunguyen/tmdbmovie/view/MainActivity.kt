package com.tranphunguyen.tmdbmovie.view

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import com.tranphunguyen.tmdbmovie.R
import com.tranphunguyen.tmdbmovie.model.MovieDBResponse
import com.tranphunguyen.tmdbmovie.service.MovieService
import com.tranphunguyen.tmdbmovie.service.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val service = MovieService.instance

        val call = service.getPopularMovies(this.getString(R.string.api_key))

        call.enqueue(object : Callback<MovieDBResponse> {
            override fun onFailure(call: Call<MovieDBResponse>, t: Throwable) {

            }

            override fun onResponse(call: Call<MovieDBResponse>, response: Response<MovieDBResponse>) {

                val movieDBResponse = response.body()



            }

        })

    }
}
