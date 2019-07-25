package com.tranphunguyen.tmdbmovie.service

import android.util.Log
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitInstance {
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


    //Cách Khác

//    @Volatile
//    private var INSTANCE: Retrofit? = null
//
//    @Synchronized
//    fun getInstance(): RetrofitInstance {
//        if (INSTANCE == null) synchronized(this) {
//            if (INSTANCE == null) {
//                INSTANCE ?: buildRetrofit()
//            }
//        }
//
//        return INSTANCE!!.create(RetrofitInstance::class.java)
//    }
//
//    private fun buildRetrofit() = retrofit2.Retrofit.Builder()
//        .baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()

}