package com.ekenya.rnd.common.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {
    private const val BASE_URL = "https://5e510330f2c0d300147c034c.mockapi.io/"
    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build() //Doesn't require the adapter
    }
    //val apiService: com.ekenya.rnd.dashboard.data.api.ApiService = getRetrofit().create(com.ekenya.rnd.dashboard.data.api.ApiService::class.java)

}