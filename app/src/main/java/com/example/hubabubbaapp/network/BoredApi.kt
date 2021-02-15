package com.example.hubabubbaapp.network

import com.example.hubabubbaapp.model.BoredResult
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.QueryMap

interface BoredApi {
    @GET("/api/activity")
    suspend fun getActivity(
        @QueryMap queries: Map<String, String>
    ): Response<BoredResult>
}