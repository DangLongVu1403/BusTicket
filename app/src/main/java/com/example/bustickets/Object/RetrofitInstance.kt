package com.example.bustickets.Object

import com.example.bustickets.Interface.ApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.1.175:4500/")
//        .baseUrl("http://192.168.55.108:4500/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiService: ApiService = retrofit.create(ApiService::class.java)
}
