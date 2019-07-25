package com.tranphunguyen.tmdbmovie.service

import android.util.Log
import com.tranphunguyen.tmdbmovie.model.MovieDBResponse
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("movie/popular")
    fun getPopularMovies(@Query("api_key") apiKey: String): Call<MovieDBResponse>

    companion object {
        val instance: MovieService by lazy {

            Log.d("TestSingleTon", "Đã tạo !!!!!")

            val retrofit = Retrofit.Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            retrofit.create(MovieService::class.java)
        }
    }

}