package com.tranphunguyen.tmdbmovie.service

import android.util.Log
import com.tranphunguyen.tmdbmovie.model.MovieDBResponse
import io.reactivex.Observable
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.concurrent.TimeUnit

interface MovieService {

//    @GET("movie/popular")
//    fun getPopularMovies(
//        @Query("page") page: Int,
//        @Query("api_key") apiKey: String
//    ): Call<MovieDBResponse>

    @GET("movie/popular")
    fun getPopularMoviesWithRx(
        @Query("page") page: Int,
        @Query("api_key") apiKey: String
    ): Observable<MovieDBResponse>

    companion object {
        val instance: MovieService by lazy {

            Log.d("TestSingleTon", "Đã tạo !!!!!")

            val okHttpClient = OkHttpClient().newBuilder()
                .connectTimeout(120, TimeUnit.SECONDS)
                .readTimeout(120, TimeUnit.SECONDS)
                .writeTimeout(120, TimeUnit.SECONDS)
                .build()

            val retrofit = Retrofit
                .Builder()
                .baseUrl("https://api.themoviedb.org/3/")
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(MovieService::class.java)
        }
    }

}