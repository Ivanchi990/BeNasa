package com.business.benasa.nasapi

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiInstance
{
    val api: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl("https://api.nasa.gov/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}