package com.example.appiumint.network

import android.util.Log
import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.Call
import retrofit2.http.GET

object RetrofitClient {
    private const val BASE_URL = "http://192.168.100.19:8012/"

    val instance: ApiService by lazy {
        Log.d("Retrofit", "Base URL: $BASE_URL")

        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        val gson = GsonBuilder()
            .setLenient()
            .create()


        retrofit.create(ApiService::class.java)
    }
}
