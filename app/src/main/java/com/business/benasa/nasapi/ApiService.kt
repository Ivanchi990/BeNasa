package com.business.benasa.nasapi

import com.business.benasa.entities.Apod
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Url

interface ApiService
{
    @GET
    fun getApod(@Url url: String): Call<Apod>
}