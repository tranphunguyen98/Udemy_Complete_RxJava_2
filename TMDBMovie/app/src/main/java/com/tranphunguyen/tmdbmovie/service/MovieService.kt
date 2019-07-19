package com.tranphunguyen.tmdbmovie.service

import com.tranphunguyen.tmdbmovie.model.MovieDBResponse
import retrofit2.Call
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieService {

    @GET("/movie/popular")
    fun getPopularMovies(@Query("apiKey") apiKey: String): Call<MovieDBResponse>



}